package com.ronnie.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ronnie.data.api.NewsApiService
import com.ronnie.data.local.NewsDatabase
import com.ronnie.data.repository.NewsRepositoryImpl
import com.ronnie.domain.models.NetworkResult
import com.ronnie.domain.models.NewsResponse
import com.ronnie.domain.repository.NewsRepository
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations.openMocks
import org.robolectric.RobolectricTestRunner
import java.io.FileInputStream
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class RepositoryTest {

    @get:Rule
    var instantTask = InstantTaskExecutorRule()
    private lateinit var database: NewsDatabase
    private lateinit var repository: NewsRepository

    @Mock
    private lateinit var apiService: NewsApiService
    private lateinit var newsResponse: NewsResponse
    private val moshi = Moshi.Builder().build()
    private val jsonAdapter = moshi.adapter(NewsResponse::class.java)

    @Before
    fun setup() {
        openMocks(this)
        val listBytes = FileInputStream("src/main/assets/response.json").readBytes()

        newsResponse = jsonAdapter.fromJson(String(listBytes))!!
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, NewsDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = NewsRepositoryImpl(apiService, newsDB = database)
    }

    @Test
    fun confirm_list_size_returns_success() = runBlocking {
        Mockito.`when`(apiService.getNews(ArgumentMatchers.anyString()))
            .thenReturn(newsResponse)
        assertEquals(apiService.getNews("any").results.size, 37)
    }

    @Test
    fun load_data_size_returns_success() = runBlocking {
        Mockito.`when`(apiService.getNews(ArgumentMatchers.anyString()))
            .thenReturn(newsResponse)
        val data = repository.getNews("home")

        assertEquals(data.first.first().size, 37)
    }

    @Test
    fun check_newsResponse_conversion_to_news_returns_success() = runBlocking {
        Mockito.`when`(apiService.getNews(ArgumentMatchers.anyString()))
            .thenReturn(newsResponse)
        val data = repository.getNews("home")

        assertEquals(
            data.first.first().first().uri,
            "nyt://article/2b8c20c5-fce1-58d6-ab52-4212525e85bb" + "home"
        )
    }

    @Test
    fun load_data_with_exception_returns_success() = runBlocking {
        val result = repository.safeApiCall { returnException() }
        assertEquals(result is NetworkResult.Failure, true)
    }

    @Test
    fun load_data_successfully_returns_success() = runBlocking {
        val result = repository.safeApiCall { }
        assertEquals(result is NetworkResult.Success, true)
    }

    private fun returnException() {
        throw IOException()
    }

    @After
    fun tearDown() {
        database.close()
    }
}