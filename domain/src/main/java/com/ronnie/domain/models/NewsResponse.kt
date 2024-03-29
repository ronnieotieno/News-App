package com.ronnie.domain.models

data class NewsResponse(
    val copyright: String,
    val last_updated: String,
    val num_results: Int,
    val results: List<News>,
    val section: String,
    val status: String
)