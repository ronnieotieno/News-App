package com.ronnie.presentation.utils

sealed class Screen(val route: String) {
    object List : Screen("list")
    object Detail : Screen("{newsUri}/detail")

    fun createRoute(newsUri: String) = "$newsUri/detail"
}