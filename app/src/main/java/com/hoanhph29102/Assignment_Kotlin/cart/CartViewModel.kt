package com.hoanhph29102.Assignment_Kotlin.cart

import com.hoanhph29102.Assignment_Kotlin.favorite.AddFavoriteRequest
import com.hoanhph29102.Assignment_Kotlin.favorite.Favorite
import com.hoanhph29102.Assignment_Kotlin.favorite.FavoriteService

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartViewModel(private val cartService: CartService) : ViewModel() {

    private val _cartProducts = MutableStateFlow<List<Cart>>(emptyList())
    val cartProducts: StateFlow<List<Cart>> = _cartProducts

//    private val _totalCartPrice = MutableStateFlow(0.0)
//    val totalCartPrice: StateFlow<Double> = _totalCartPrice

    private val _totalCartPrice = MutableLiveData(0.0)
    val totalCartPrice : LiveData<Double> = _totalCartPrice

    private val _isLoading = MutableLiveData(false)
    val isLoading : LiveData<Boolean> = _isLoading

    fun fetchCart(userId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val carts = withContext(Dispatchers.IO) {
                    cartService.getCart(userId)
                }
                _cartProducts.value = carts
                calculateTotalMoney(carts)
            } catch (e: Exception) {
                Log.e("CartViewModel", "Error fetching carts: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }

    }

    fun addToCart(userId: String, productId: String, quantity: Int){
        try {
            viewModelScope.launch {
                withContext(Dispatchers.IO){
                    cartService.addToCart(AddToCart(userId, productId,quantity))
                }
                fetchCart(userId)

            }
        }catch (e: Exception){
            Log.e("CartViewModel", "addToCart: ${e.message}", )
        }
    }

//    fun updateCartQuantity1(cartItemId: String, quantity: Int){
//        try {
//            viewModelScope.launch {
//                withContext(Dispatchers.IO){
//                    cartService.updateQuantity(cartItemId, quantity)
//                }
//            }
//        }catch (e: Exception){
//            Log.e("CartViewModel", "updateCartQuantity: ${e.message}", e)
//        }
//    }

    fun updateCartQuantity(cartItemId: String, newQuantity: Int) {
        viewModelScope.launch {
            try {
                val response = cartService.updateQuantity(cartItemId, newQuantity)
                if (response.isSuccessful) {
                    val updatedCartItem = response.body()
                    updatedCartItem?.let { updatedItem ->
                        _cartProducts.value = _cartProducts.value.map { cartItem ->
                            if (cartItem._id == updatedItem._id) {
                                cartItem.copy(
                                    quantity = updatedItem.quantity,
                                    totalCartItem = updatedItem.priceProduct * updatedItem.quantity
                                )
                            } else cartItem
                        }
                        calculateTotalMoney(_cartProducts.value)
                    }

                } else {
                    // Handle error
                }

            } catch (e: Exception) {
                // Handle exception
            }
        }
    }

    private fun calculateTotalMoney(carts: List<Cart>){
        val total = carts.sumOf { it.totalCartItem }
        _totalCartPrice.value = total
    }
    fun getTotalMoney(userId: String) {
        viewModelScope.launch {
            try {
                val response = cartService.getTotalMoney(userId)
                if (response.isSuccessful){
                    val totalMoneyResponse = response.body()
                    totalMoneyResponse?.let {
                        _totalCartPrice.value = it.totalMoney
                    }
                }else{
                    //
                }
            }catch (e: Exception) {
                // Handle exception
            }
        }
    }

    fun deleteCartItem(cartItemId: String, userId: String){
        try {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    cartService.deleteCartItem(cartItemId)
                }
                fetchCart(userId)
            }

        }catch (e :Exception){
            Log.e("TAG", "deleteCartItem: $e", )
        }

    }

}

class CartViewModelFactory(private val cartService: CartService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(cartService = cartService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

