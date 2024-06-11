package com.hoanhph29102.Assignment_Kotlin.address

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import com.hoanhph29102.Assignment_Kotlin.profile.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ShippingAddressViewModel : ViewModel() {
    var addressList by mutableStateOf<List<Address>>(emptyList())
        private set

    init {
        loadAddresses()
    }

    private fun loadAddresses() {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                try {
                    val document = FirebaseFirestore.getInstance().collection("user").document(userId).get().await()
                    if (document.exists()) {
                        val user = document.toObject(User::class.java)
                        addressList = user?.addresses ?: emptyList()
                    }
                } catch (e: Exception) {
                    Log.e("viewModelAddress", "loadAddresses: $e")
                }
            }
        }
    }

    suspend fun updateDefaultAddress(selectedAddress: Address, isDefault: Boolean) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            try {
                val firestore = FirebaseFirestore.getInstance()
                val userDocument = firestore.collection("user").document(userId).get().await()
                if (userDocument.exists()) {
                    val user = userDocument.toObject(User::class.java)
                    user?.addresses?.forEach { address ->
                        if (address.idAddress == selectedAddress.idAddress) {
                            address.isDefault = isDefault
                        } else {
                            address.isDefault = false
                        }
                    }
                    firestore.collection("user").document(userId).set(user!!).await()
                    addressList = user.addresses
                }
            } catch (e: Exception) {
                Log.e("viewModel update address default", "updateDefaultAddress: $e")
            }
        }
    }
}