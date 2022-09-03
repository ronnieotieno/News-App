package com.ronnie.domain.models.uiView

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_table")
data class NewsView(
    @PrimaryKey val uri: String,
    val byline: String,
    val imageUrl: String?,
    val description: String,
    val title: String,
    val url: String,
    val category: String,
    var createdAt: Long?
)
