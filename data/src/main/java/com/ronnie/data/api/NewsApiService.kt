package com.ronnie.data.api

import com.ronnie.domain.models.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface NewsApiService {

    @GET("{category}.json")
    suspend fun getNews(
        @Path("category") category: String
    ): NewsResponse
}