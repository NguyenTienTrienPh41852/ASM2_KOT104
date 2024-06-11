package com.hoanhph29102.Assignment_Kotlin.product

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductViewModel : ViewModel() {
    private val productService = ProductService.getInstance()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _productDetails = MutableStateFlow<Product?>(null)
    val productDetails: StateFlow<Product?> = _productDetails

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            try {
                val productList = withContext(Dispatchers.IO) {
                    productService.getProducts()
                }
                _products.value = productList
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Error fetching products: ${e.message}", e)
            }
        }
    }

    fun getProductDetails(productId: String) {
        viewModelScope.launch {
            try {
                val product = withContext(Dispatchers.IO) {
                    productService.getProductDetails(productId)
                }
                _productDetails.value = product
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Error fetching product details: ${e.message}", e)
            }
        }
    }

}

