package com.ronnie.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ronnie.presentation.NewsViewModel
import com.ronnie.presentation.R
import com.ronnie.presentation.screens.newsCategories
import com.ronnie.presentation.screens.selectedCategory
import java.util.Locale

@Composable
fun HomeHeader(viewModel: NewsViewModel) {
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
        ChipGroup(
            newsCategories,
            selectedCategory = selectedCategory.value,
            onSelectedChanged = {
                selectedCategory.value = it
                selectedCategory.value?.lowercase(Locale.ROOT)
                    .let { category ->
                        if (category != null) {
                            viewModel.getNews(category)
                        }
                    }
            })
    }
}