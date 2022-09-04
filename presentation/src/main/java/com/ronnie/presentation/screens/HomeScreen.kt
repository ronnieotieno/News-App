package com.ronnie.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ronnie.domain.models.UiState
import com.ronnie.presentation.NewsViewModel
import com.ronnie.presentation.R
import com.ronnie.presentation.components.ChipGroup
import com.ronnie.presentation.components.LoadingShimmerEffect
import com.ronnie.presentation.components.NewsItem
import java.util.Locale

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun HomeScreen(viewModel: NewsViewModel = hiltViewModel()) {

    val selectedCategory: MutableState<String?> = remember {
        mutableStateOf(newsCategories[0])
    }
    val listState = rememberLazyListState()

    val news: UiState by viewModel.newsList.collectAsStateWithLifecycle()

    Column(
        Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(R.drawable.nyt_logo)
                .crossfade(true)
                .build(),
            contentDescription = "news Image",
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp, start = 10.dp),
            contentScale = ContentScale.Inside
        )
        Text(
            text = "Top Stories",
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.primaryTextColor),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = TextStyle(fontSize = 30.sp, fontFamily = FontFamily.Serif),
            maxLines = 1,
        )
        Spacer(Modifier.height(5.dp))
        ChipGroup(newsCategories, selectedCategory = selectedCategory.value, onSelectedChanged = {
            selectedCategory.value = it
            selectedCategory.value?.lowercase(Locale.ROOT)
                .let { category -> viewModel.getNews(category!!) }
        })
        LazyColumn(state = listState) {
            if (news.data.isNotEmpty() && !news.isLoading) {
                items(news.data) { item ->
                    NewsItem(newsView = item)
                }
            } else if (news.isLoading) {
                items(10) {
                    LoadingShimmerEffect()
                }
            }
        }
    }
}

val newsCategories get() = listOf("Home", "Arts", "Science", "US", "World")