package com.ronnie.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ronnie.presentation.components.ChipGroup

val selectedCategory: MutableState<String?> = mutableStateOf(newsCategories[0])

@Composable
fun HomeScreen(){
    Column(
        Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        ChipGroup(newsCategories, selectedCategory = selectedCategory.value,onSelectedChanged = { selectedCategory.value = it })
    }
}

val newsCategories get() = listOf("Home", "Arts", "Science", "US", "World" )