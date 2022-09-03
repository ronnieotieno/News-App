package com.ronnie.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ronnie.domain.models.UiState
import com.ronnie.domain.useCases.NewsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val newsListUseCase: NewsListUseCase):ViewModel() {

    private val _newResponse: MutableStateFlow<UiState> =
        MutableStateFlow(UiState(true, emptyList(), false))
    val newsList get() = _newResponse

    fun getNews(){
        viewModelScope.launch {
            _newResponse.emit(newsListUseCase.invoke("arts"))
        }
    }
}