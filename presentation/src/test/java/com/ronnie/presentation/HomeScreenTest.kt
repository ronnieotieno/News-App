package com.ronnie.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.text.TextLayoutResult
import androidx.core.graphics.toColorInt
import androidx.navigation.compose.rememberNavController
import com.ronnie.domain.models.UiState
import com.ronnie.domain.models.uiView.NewsView
import com.ronnie.presentation.screens.HomeScreen
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    var instantTask = InstantTaskExecutorRule()

    @Mock
    private lateinit var viewModel: NewsViewModel
    private val fakeNews = NewsView("","","","","","","",0L)

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun show_loading_screen_should_pass() {
        Mockito.`when`(viewModel.newsList)
            .thenReturn(MutableStateFlow(UiState(true, emptyList(), false)))
        composeTestRule.setContent {
            HomeScreen(navController = rememberNavController(), viewModel = viewModel)
        }
        composeTestRule.onAllNodesWithTag("loader")[0].assertIsDisplayed()
    }

    @Test
    fun show_data_list_screen_should_pass() {
        Mockito.`when`(viewModel.newsList)
            .thenReturn(MutableStateFlow(UiState(false, listOf(fakeNews), false)))
        composeTestRule.setContent {
            HomeScreen(navController = rememberNavController(), viewModel = viewModel)
        }
        composeTestRule.onAllNodesWithTag("item")[0].assertIsDisplayed()
    }

    @Test
    fun show_error_screen_should_pass() {
        Mockito.`when`(viewModel.newsList)
            .thenReturn(MutableStateFlow(UiState(false, emptyList(), true)))
        composeTestRule.setContent {
            HomeScreen(navController = rememberNavController(), viewModel = viewModel)
        }
        composeTestRule.onNodeWithTag("error").assertIsDisplayed()
    }

    @Test
    fun check_chip_selection_should_pass() {
        Mockito.`when`(viewModel.newsList)
            .thenReturn(MutableStateFlow(UiState(false, emptyList(), true)))
        composeTestRule.setContent {
            HomeScreen(navController = rememberNavController(), viewModel = viewModel)
        }
        composeTestRule.onAllNodesWithText("Arts")[0].performClick()
        composeTestRule.onAllNodesWithText("Arts")[0].assertTextColor(Color.White)
    }

    @Test
    fun check_inactive_chip_selection_should_pass() {
        Mockito.`when`(viewModel.newsList)
            .thenReturn(MutableStateFlow(UiState(false, emptyList(), true)))
        composeTestRule.setContent {
            HomeScreen(navController = rememberNavController(), viewModel = viewModel)
        }
        composeTestRule.onAllNodesWithText("Home")[0].performClick()
        composeTestRule.onAllNodesWithText("Arts")[0].assertTextColor(Color("#253046".toColorInt()))
    }

    private fun SemanticsNodeInteraction.assertTextColor(
        color: Color
    ): SemanticsNodeInteraction = assert(isOfColor(color))

    private fun isOfColor(color: Color): SemanticsMatcher = SemanticsMatcher(
        "${SemanticsProperties.Text.name} is of color '$color'"
    ) {
        val textLayoutResults = mutableListOf<TextLayoutResult>()
        it.config.getOrNull(SemanticsActions.GetTextLayoutResult)
            ?.action
            ?.invoke(textLayoutResults)
        return@SemanticsMatcher if (textLayoutResults.isEmpty()) {
            false
        } else {
            textLayoutResults.first().layoutInput.style.color == color
        }
    }
}