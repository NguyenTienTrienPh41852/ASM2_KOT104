package com.hoanhph29102.Assignment_Kotlin.paymentMethod

data class PaymentMethod(
    val idMethod: String = "",
    val cardNumber: String = "",
    val cvv: String = "",
    val expirationDate: String = "",
    var isDefault: Boolean = false,
)