package com.ronnie.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ronnie.domain.models.UiState
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
    private var collectJob:Job? = null

    init {
        getNews("home")
    }

    fun getNews(selectedCategory:String) {
        collectJob?.cancel()
        collectJob =  viewModelScope.launch(Dispatchers.IO) {
            _newResponse.emit(UiState(true, emptyList(), false))
            newsListUseCase.invoke(selectedCategory).apply {
               first.collectLatest { list ->
                    Log.d("SelectedCategory", selectedCategory)
                    _newResponse.emit(UiState(false, list.filter { it.byline.isNotEmpty() && it.title.isNotEmpty() }, second))
                }
            }
        }
    }
}