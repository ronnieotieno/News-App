package com.ronnie.domain.repository

import com.ronnie.domain.models.NetworkResult
import com.ronnie.domain.models.uiView.NewsView
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getNews(category: String): Pair<Flow<List<NewsView>>, Boolean>
    suspend fun <T> safeApiCall(apiCall: suspend () -> T): NetworkResult<T>
}