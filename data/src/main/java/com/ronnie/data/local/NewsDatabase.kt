package com.ronnie.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ronnie.domain.models.uiView.NewsView
import javax.inject.Singleton

@Database(
    entities = [NewsView::class],
    version = 2, exportSchema = false
)
@Singleton
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}