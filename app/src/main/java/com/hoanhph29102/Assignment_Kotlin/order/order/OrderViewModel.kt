package com.hoanhph29102.Assignment_Kotlin.order.order

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hoanhph29102.Assignment_Kotlin.address.Address
import com.hoanhph29102.Assignment_Kotlin.paymentMethod.PaymentMethod
import com.hoanhph29102.Assignment_Kotlin.profile.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class OrderViewModel(private val orderService: OrderService) : ViewModel(){
    private val _oderProduct = MutableStateFlow<List<Order>>(emptyList())
    val orderProducts : StateFlow<List<Order>> = _oderProduct

    private val _defaultAddress = MutableStateFlow<Address?>(null)
    val defaultAddress: StateFlow<Address?> = _defaultAddress

    private val _defaultPayment = MutableStateFlow<PaymentMethod?>(null)
    val defaultPayment: StateFlow<PaymentMethod?> = _defaultPayment

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _orderDetail = MutableStateFlow<Order?>(null)
    val orderDetail: StateFlow<Order?> = _orderDetail

    fun fetDefaultUser(){
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val userId = user.uid
            viewModelScope.launch {
                try {
                    val userSnapshot = db.collection("users").document(userId).get().await()
                    val user = userSnapshot.toObject(User::class.java)
                    user?.let {
                        _defaultAddress.value = it.addresses.find { address -> address.isDefault }
                        _defaultPayment.value = it.paymentMethods.find { payment -> payment.isDefault }
                    }
                } catch (e: Exception) {
                    Log.e("OrderViewModel", "fetchDefaultUserInfo: $e")
                }
            }
        }
    }
    fun fetchOrder(userId: String){
        viewModelScope.launch {
            try {
                val orders = withContext(Dispatchers.IO){
                    orderService.getOrder(userId)
                }
                _oderProduct.value = orders
            }catch (e: Exception){
                Log.e("TAG", "fetchOrder: $e", )
            }
        }
    }

    fun createOrder(userId: String, name: String, address: String, payment: String){
        viewModelScope.launch {
            try {
                val orderRequest = OrderRequest(
                    nameUser = name,
                    addressUser = address,
                    paymentUser = payment
                )

                val order = withContext(Dispatchers.IO) {
                    orderService.createOrder(userId, orderRequest)
                }
                _oderProduct.value = _oderProduct.value + order
            } catch (e: Exception) {
                Log.e("OrderViewModel", "createOrder: $e")
            }
        }
    }

    fun getDetailOrder(orderId : String){
        viewModelScope.launch {
            try {
                val order = withContext(Dispatchers.IO) {
                    orderService.getOrderDetail(orderId)
                }
                _orderDetail.value = order
            } catch (e: Exception) {
                Log.e("OrderViewModel", "Error fetching order details: ${e.message}", e)
            }
        }
    }

    fun clearCart(userId: String){
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO){
                    orderService.clearCart(userId)
                }
            }catch (e: Exception){
                Log.e("TAG", "clearCart: $e", )
            }
        }
    }

}

class OrderViewModelFactory(private val orderService: OrderService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrderViewModel(orderService = orderService)  as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}