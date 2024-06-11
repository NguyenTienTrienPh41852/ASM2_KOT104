package com.hoanhph29102.Assignment_Kotlin.favorite

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteViewModel(private val favoriteService: FavoriteService) : ViewModel() {

    private val _favoriteProducts = MutableStateFlow<List<Favorite>>(emptyList())
    val favoriteProducts: StateFlow<List<Favorite>> = _favoriteProducts

    fun fetchFavoriteProducts(userId: String) {
        viewModelScope.launch {
            try {
                val favorites = withContext(Dispatchers.IO) {
                    favoriteService.getFavoriteProducts(userId)
                }
                _favoriteProducts.value = favorites
            } catch (e: Exception) {
                Log.e("FavoriteViewModel", "Error fetching favorite products: ${e.message}", e)
            }
        }

    }

    fun addToFavorites(userId: String, productId: String) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    favoriteService.addToFavorites(AddFavoriteRequest(userId, productId))
                }
                fetchFavoriteProducts(userId)
            } catch (e: Exception) {
                Log.e("FavoriteViewModel", "Error adding to favorites: ${e.message}", e)
            }
        }
    }

    fun removeFromFavorites(userId: String, productId: String) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    favoriteService.removeFromFavorites(userId, productId)
                }
                fetchFavoriteProducts(userId)
            } catch (e: Exception) {
                Log.e("FavoriteViewModel", "Error removing from favorites: ${e.message}", e)
            }
        }
    }

    fun deleteFavItem(userId: String, favItemId: String) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    favoriteService.deleteFavItem(favItemId)
                }
                fetchFavoriteProducts(userId)
            } catch (e: Exception) {
                Log.e("FavoriteViewModel", "Error removing from favorites: ${e.message}", e)
            }
        }
    }

}

class FavoriteViewModelFactory(private val favoriteService: FavoriteService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoriteViewModel(favoriteService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

