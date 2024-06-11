package com.hoanhph29102.Assignment_Kotlin.cart

import com.hoanhph29102.Assignment_Kotlin.api.Constants
import com.hoanhph29102.Assignment_Kotlin.favorite.AddFavoriteRequest
import com.hoanhph29102.Assignment_Kotlin.favorite.Favorite
import com.hoanhph29102.Assignment_Kotlin.favorite.FavoriteService
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CartService{

    @POST("/cart")
    suspend fun addToCart(@Body request: AddToCart)

    @GET("/cart/{userId}")
    suspend fun getCart(@Path("userId") userId: String): List<Cart>

    @GET("/cart/total-money/{userId}")
    suspend fun getTotalMoney(@Path("userId") userId: String): Response<TotalMoneyResponse>

    @FormUrlEncoded
    @PUT("cart/update-quantity/{cartItemId}")
    suspend fun updateQuantity(@Path("cartItemId") cartItemId: String, @Field("quantity") quantity: Int): Response<Cart>

    @DELETE("cart/{cartItemId}")
    suspend fun deleteCartItem(@Path("cartItemId") cartItemId: String)

    companion object {
        private var retrofitService: CartService? = null
        fun getInstance(): CartService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(Constants.DOMAIN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(CartService::class.java)
            }
            return retrofitService!!
        }
    }
}