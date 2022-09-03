package com.ronnie.domain.models

import com.ronnie.domain.models.uiView.NewsView

data class UiState(
    val isLoading: Boolean = false,
    val data: List<NewsView>,
    val error: Boolean = false
)