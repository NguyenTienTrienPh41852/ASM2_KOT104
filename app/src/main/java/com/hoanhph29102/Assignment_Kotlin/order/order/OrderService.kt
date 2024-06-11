package com.hoanhph29102.Assignment_Kotlin.order.order

import com.hoanhph29102.Assignment_Kotlin.api.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OrderService{
    @GET("order/{userId}")
    suspend fun getOrder(@Path("userId") userId: String) : List<Order>

    @POST("order/{userId}")
    suspend fun createOrder(@Path("userId") userId: String, @Body orderRequest: OrderRequest) : Order

    @GET("order/orderDetail/{orderId}")
    suspend fun getOrderDetail(@Path("orderId") orderId: String) : Order

    @POST("order/clear-cart/{userId}")
    suspend fun clearCart(@Path("userId") userId: String)
    companion object {
        private var retrofitService: OrderService? = null
        fun getInstance(): OrderService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(Constants.DOMAIN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(OrderService::class.java)
            }
            return retrofitService!!
        }
    }
}