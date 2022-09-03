package com.ronnie.domain.useCases

import com.ronnie.domain.models.uiView.NewsView
import com.ronnie.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsListUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    suspend fun invoke(category: String): Pair<Flow<List<NewsView>>, Boolean> {
        return newsRepository.getNews(category)
    }
}