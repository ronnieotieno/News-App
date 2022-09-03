package com.ronnie.domain.repository

import com.ronnie.domain.models.UiState

interface NewsRepository {
    suspend fun getNews(category: String): UiState
}