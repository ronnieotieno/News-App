package com.ronnie.presentation.navGraph

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.ronnie.presentation.NewsViewModel
import com.ronnie.presentation.screens.DetailScreen
import com.ronnie.presentation.screens.HomeScreen
import com.ronnie.presentation.utils.Screen

@Composable
@OptIn(ExperimentalAnimationApi::class)
fun MainNavGraph() {
    val navController = rememberAnimatedNavController()
    val viewModel: NewsViewModel = hiltViewModel()
    AnimatedNavHost(navController, startDestination = Screen.List.route) {
        composable(route = Screen.List.route) {
            HomeScreen(navController, viewModel)
        }
        composable(route = Screen.Detail.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }) { backStackEntry ->
            val uri = backStackEntry.arguments?.getString("newsUri")
            requireNotNull(uri) { "Uri parameter wasn't found. Please make sure it's set!" }
            DetailScreen(navController, uri, viewModel)
        }
    }
}
