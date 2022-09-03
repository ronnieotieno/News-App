package com.ronnie.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ronnie.domain.useCases.NewsListUseCase
import com.ronnie.presentation.theme.NYTNewsTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var newsListUseCase: NewsListUseCase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NYTNewsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                )  {
                    Surface2()
                }
            }
        }
    }
}

@Composable
fun Surface2(viewModel: NewsViewModel = hiltViewModel()) {
    val news  = remember {
        viewModel.getNews()
        viewModel.newsList
    }.collectAsState()

    Scaffold(
        content = {
            when {
                news.value.isLoading -> {
                    ShowData("Loading")
                }
                news.value.data.isNotEmpty() -> {
                    ShowData(news.value.toString())
                }
                news.value.error -> {
                    ShowData("Error")
                }
            }
        })
}

@Composable
fun ShowData(data: String) {
    Text(text = data)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NYTNewsTheme {
        ShowData("Android")
    }
}