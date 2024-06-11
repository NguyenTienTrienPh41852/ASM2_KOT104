package com.hoanhph29102.Assignment_Kotlin.order.order

data class Order(
    val _id: String,
    val OrderID: Int,
    val userId: String,
    val cartId: String,
    val totalMoney: Double,
    val totalQuantity: Int,
    val nameUser: String,
    val addressUser:String,
    val paymentUser: String,
    val date: String,
    val items: List<OrderItem>
)
data class OrderItem(
    val nameProduct: String,
    val quantity: String,
)

data class OrderRequest(
    val nameUser: String,
    val addressUser: String,
    val paymentUser: String
)