package com.ronnie.domain.useCases

import com.ronnie.domain.models.UiState
import com.ronnie.domain.repository.NewsRepository
import javax.inject.Inject

class NewsListUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    suspend fun invoke(category:String): UiState {
        return newsRepository.getNews(category)
    }
}