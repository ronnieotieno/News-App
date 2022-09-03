package com.ronnie.data.repository

import com.ronnie.data.api.NewsApiService
import com.ronnie.domain.models.NetworkResult
import com.ronnie.domain.models.News
import com.ronnie.domain.models.UiState
import com.ronnie.domain.models.uiView.NewsView
import com.ronnie.domain.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(private val apiService: NewsApiService) : NewsRepository {
    override suspend fun getNews(category: String): UiState {
        val data = safeApiCall { apiService.getNews(category) }

        return if (data is NetworkResult.Success) {
            val newsViewList: List<NewsView> = data.value.results.map { news ->
                news.toNewsView()
            }
            UiState(false, newsViewList, false)
        } else {
            UiState(false, emptyList(), true)
        }
    }

    private suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): NetworkResult<T> {
        return withContext(Dispatchers.IO) {
            try {
                NetworkResult.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        NetworkResult.Failure(
                            false,
                            throwable.code(),
                            throwable.response()?.errorBody()
                        )
                    }
                    else -> {
                        NetworkResult.Failure(true, null, null)
                    }
                }
            }
        }
    }

    private fun News.toNewsView() = NewsView(
        byline = byline,
        multimedia = multimedia,
        title = title,
        description = abstract,
        url = url
    )
}