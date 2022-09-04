package com.ronnie.data.repository

import com.ronnie.data.api.NewsApiService
import com.ronnie.data.local.NewsDatabase
import com.ronnie.domain.models.NetworkResult
import com.ronnie.domain.models.News
import com.ronnie.domain.models.uiView.NewsView
import com.ronnie.domain.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val apiService: NewsApiService, private val newsDB: NewsDatabase
) : NewsRepository {

    private val newsDao = newsDB.newsDao()

    override suspend fun getNews(category: String): Pair<Flow<List<NewsView>>, Boolean> {
        val data = safeApiCall { apiService.getNews(category) }

        val hasError = if (data is NetworkResult.Success) {
            val newsViewList: List<NewsView> = data.value.results.map { news ->
                news.toNewsView(category)
            }
            newsDao.insertAll(newsViewList)
            false
        } else {
            true
        }

        return Pair(newsDao.getCategoryNews(category), hasError)
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

    private fun News.toNewsView(category: String) = NewsView(
        byline = byline,
        imageUrl = if (multimedia != null && multimedia?.isNotEmpty() == true) multimedia!![0].url else null,
        title = title,
        description = abstract,
        url = url,
        uri = uri + category,
        createdAt = System.currentTimeMillis(),
        category = category
    )
}