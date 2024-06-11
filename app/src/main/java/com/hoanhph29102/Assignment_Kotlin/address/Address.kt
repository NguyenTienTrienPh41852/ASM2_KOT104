package com.hoanhph29102.Assignment_Kotlin.address

data class Address(
    val idAddress : String = "",
    val address: String = "",
    val country: String = "",
    val city: String = "",
    val district: String = "",
    var isDefault : Boolean = false
)