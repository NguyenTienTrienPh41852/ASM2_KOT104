package com.hoanhph29102.Assignment_Kotlin.favorite

import com.hoanhph29102.Assignment_Kotlin.api.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FavoriteService {
    @POST("/favorite")
    suspend fun addToFavorites(@Body request: AddFavoriteRequest)

    @POST("/favorite")
    suspend fun addToFavorites1(
        @Body request: AddFavoriteRequest
    )

    @GET("/favorite/{userId}")
    suspend fun getFavoriteProducts(@Path("userId") userId: String): List<Favorite>

    @DELETE("/favorite/{userId}/{productId}")
    suspend fun removeFromFavorites(
        @Path("userId") userId: String,
        @Path("productId") productId: String
    )

    @DELETE("/favorite/{favItemId}")
    suspend fun deleteFavItem(@Path("favItemId") favItemId : String)

    companion object {
        private var retrofitService: FavoriteService? = null
        fun getInstance(): FavoriteService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(Constants.DOMAIN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(FavoriteService::class.java)
            }
            return retrofitService!!
        }
    }
}