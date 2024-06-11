package com.hoanhph29102.Assignment_Kotlin.favorite

data class Favorite(
    val _id: String,
    val userId: String,
    val productId: String,
    val isFavorite: Boolean,
    val nameProduct: String,
    val priceProduct: Double,
    val imageProduct: String
)

data class AddFavoriteRequest(
    val userId: String,
    val productId: String
)
data class DeleteFavorite(
    val userId: String,
    val productId: String
)