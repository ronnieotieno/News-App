package com.ronnie.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ronnie.domain.models.uiView.NewsView
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(news: List<NewsView>)

    @Query("SELECT * FROM news_table WHERE category=:category ORDER BY createdAt ASC")
    fun getCategoryNews(category: String): Flow<List<NewsView>>
}