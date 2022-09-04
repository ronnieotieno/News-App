package com.ronnie.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ronnie.domain.models.UiState
import com.ronnie.presentation.NewsViewModel
import com.ronnie.presentation.ShowData
import com.ronnie.presentation.components.ChipGroup
import com.ronnie.presentation.components.LoadingShimmerEffect
import com.ronnie.presentation.components.NewsItem
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale


@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun HomeScreen(viewModel: NewsViewModel = hiltViewModel()){

    val selectedCategory: MutableState<String?> = remember {
        mutableStateOf(newsCategories[0])
    }
    val listState = rememberLazyListState()

    val news:UiState by viewModel.newsList.collectAsStateWithLifecycle()

    Column(
        Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        ChipGroup(newsCategories, selectedCategory = selectedCategory.value,onSelectedChanged = {
            Log.d("SelectedCategory", it)
            selectedCategory.value = it
            selectedCategory.value?.lowercase(Locale.ROOT).let { category-> viewModel.getNews(category!!) }
        })
        LazyColumn(state = listState){
            if (news.data.isNotEmpty() && !news.isLoading ) {
                items(news.data) { item ->
                    NewsItem(newsView = item )
                }
            } else if (news.isLoading) {
                items(10) {
                    LoadingShimmerEffect()
                }
            }
        }
    }
}

val newsCategories get() = listOf("Home", "Arts", "Science", "US", "World" )