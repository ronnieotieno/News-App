package com.ronnie.domain.models.uiView

import com.ronnie.domain.models.Multimedia

data class NewsView(
    val byline: String,
    val multimedia: List<Multimedia>?,
    val description: String,
    val title: String,
    val url: String
)
