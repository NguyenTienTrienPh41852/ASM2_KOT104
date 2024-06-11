package com.hoanhph29102.Assignment_Kotlin.paymentMethod

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


class PaymentMethodViewModel : ViewModel() {
    var paymentMethodList by mutableStateOf<List<PaymentMethod>>(emptyList())
        private set

    init {
        loadPaymentMethods()
    }

    private fun loadPaymentMethods() {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                try {
                    val document = FirebaseFirestore.getInstance().collection("user").document(userId).get().await()
                    if (document.exists()) {
                        val user = document.toObject(User::class.java)
                        paymentMethodList = user?.paymentMethods ?: emptyList()
                    }
                } catch (e: Exception) {
                    Log.e("PaymentMethodViewModel", "loadPaymentMethods: $e")
                }
            }
        }
    }

    suspend fun updateDefaultPaymentMethod(selectedMethod: PaymentMethod, isDefault: Boolean) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            try {
                val firestore = FirebaseFirestore.getInstance()
                val userDocument = firestore.collection("user").document(userId).get().await()
                if (userDocument.exists()) {
                    val user = userDocument.toObject(User::class.java)
                    user?.paymentMethods?.forEach { method ->
                        if (method.idMethod == selectedMethod.idMethod) {
                            method.isDefault = isDefault
                        } else {
                            method.isDefault = false
                        }
                    }
                    firestore.collection("user").document(userId).set(user!!).await()
                    paymentMethodList = user.paymentMethods
                }
            } catch (e: Exception) {
                Log.e("PaymentMethodViewModel", "updateDefaultPaymentMethod: $e")
            }
        }
    }
}