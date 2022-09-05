package com.ronnie.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ronnie.domain.models.UiState
import com.ronnie.domain.models.uiView.NewsView
import com.ronnie.domain.useCases.NewsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val newsListUseCase: NewsListUseCase) :
    ViewModel() {

    private val _newResponse: MutableStateFlow<UiState> =
        MutableStateFlow(UiState(true, emptyList(), false))
    val newsList get() = _newResponse
    private var collectJob: Job? = null
    var currentNewsList: List<NewsView> = emptyList()
    var currentCategory = "home"

    init {
        getNews("home")
    }

    fun getNews(selectedCategory: String) {
        collectJob?.cancel()
        currentCategory = selectedCategory
        collectJob = viewModelScope.launch(Dispatchers.IO) {
            _newResponse.emit(UiState(true, emptyList(), false))
            newsListUseCase.invoke(selectedCategory).apply {
                first.collectLatest { list ->
                    Log.d("SelectedCategory", selectedCategory)
                    val filteredList =
                        list.filter { it.byline.isNotEmpty() && it.title.isNotEmpty() }
                    currentNewsList = filteredList
                    _newResponse.emit(
                        UiState(
                            isLoading = false,
                            data = filteredList,
                            error = second
                        )
                    )
                }
            }
        }
    }
}