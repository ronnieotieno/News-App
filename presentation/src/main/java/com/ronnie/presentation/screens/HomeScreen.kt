package com.ronnie.presentation.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ronnie.domain.models.UiState
import com.ronnie.presentation.NewsViewModel
import com.ronnie.presentation.components.ErrorView
import com.ronnie.presentation.components.HomeHeader
import com.ronnie.presentation.components.LoadingShimmerEffect
import com.ronnie.presentation.components.NewsItem
import kotlinx.coroutines.flow.collectLatest

val selectedCategory: MutableState<String?> = mutableStateOf(newsCategories[0])

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: NewsViewModel) {

    val listState = rememberLazyListState()

    val news: UiState by viewModel.newsList.collectAsStateWithLifecycle()

    val snackHostState = SnackbarHostState()
    val scaffoldState = rememberScaffoldState(snackbarHostState = snackHostState)

    LaunchedEffect(Unit) {
        viewModel.newsList.collectLatest { news ->
            if (news.error && news.data.isNotEmpty()) {
                snackHostState.showSnackbar(
                    message = "Unable to fetch new articles, check your internet connection",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }
    Scaffold(scaffoldState = scaffoldState) {
        LazyColumn(state = listState) {
            item {
                HomeHeader(viewModel = viewModel)
            }

            if (news.error && news.data.isEmpty()) {
                item {
                    ErrorView { viewModel.getNews(viewModel.currentCategory) }
                }
            } else {
                if (news.data.isNotEmpty() && !news.isLoading) {
                    items(news.data) { item ->
                        NewsItem(navController = navController, newsView = item)
                    }
                } else if (news.isLoading) {
                    items(10) {
                        LoadingShimmerEffect()
                    }
                }
            }
        }
    }
}

val newsCategories get() = listOf("Home", "Arts", "Science", "US", "World")