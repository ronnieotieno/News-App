package com.ronnie.domain.repository

import com.ronnie.domain.models.uiView.NewsView
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getNews(category: String): Pair<Flow<List<NewsView>>, Boolean>
}