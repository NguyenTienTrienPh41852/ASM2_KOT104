package com.hoanhph29102.Assignment_Kotlin.profile

import com.hoanhph29102.Assignment_Kotlin.address.Address
import com.hoanhph29102.Assignment_Kotlin.paymentMethod.PaymentMethod

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String? = "",
    var addresses: List<Address> = listOf(),
    var paymentMethods: List<PaymentMethod> = listOf()
)