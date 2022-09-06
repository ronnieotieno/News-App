package com.ronnie.presentation.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ronnie.presentation.NewsViewModel
import com.ronnie.presentation.R
import com.ronnie.presentation.components.Loader

@Composable
fun DetailScreen(navController: NavHostController, uri: String, viewModel: NewsViewModel) {

    val ctx = LocalContext.current
    val selectedNews = viewModel.currentNewsList.find { it.uri.substringAfterLast("/") == uri }!!
    val showWebView: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
    val loaderVisibility: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
    Box(
        Modifier
            .fillMaxWidth()
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(selectedNews.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "news Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Scaffold(
            backgroundColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    elevation = 0.dp,
                    backgroundColor = Color.Transparent,
                    title = { Text(color = Color.White, text = "Detail") },
                    navigationIcon = {
                        IconButton(onClick = {
                            loaderVisibility.value = false
                            if (showWebView.value) showWebView.value =
                                false else navController.navigateUp()
                        }) {
                            Icon(
                                imageVector =  if (showWebView.value) Icons.Filled.Close  else Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    contentColor = Color.White,
                )
            },
            content = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Surface(
                        color = Color.Black.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(topEnd = 8.dp, topStart = 8.dp),
                    ) {
                        if (loaderVisibility.value) {
                            Loader()
                        }
                        if (showWebView.value) {
                            LoadWebUrl(ctx, selectedNews.url, loaderVisibility)
                        } else {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = selectedNews.title,
                                    color = Color.White,
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.height(10.dp))
                                Text(
                                    text = selectedNews.byline,
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                                Spacer(Modifier.height(10.dp))
                                Text(
                                    text = selectedNews.description,
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                                Spacer(Modifier.height(5.dp))
                                Row(modifier = Modifier.clickable {
                                    showWebView.value = true; loaderVisibility.value = true
                                }) {
                                    Text(
                                        text = "See More",
                                        color = colorResource(id = R.color.bright_blue),
                                        fontSize = 15.sp
                                    )
                                    Icon(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .padding(start = 2.dp),
                                        tint = colorResource(id = R.color.bright_blue),
                                        painter = painterResource(id = R.drawable.ic_open_link),
                                        contentDescription = ""
                                    )
                                }
                                Spacer(Modifier.height(20.dp))
                            }
                        }
                    }
                }

            })
    }
}

@Composable
fun LoadWebUrl(ctx: Context, url: String, loaderVisibility: MutableState<Boolean>) {
    AndroidView(factory = {
        WebView(ctx).apply {
            //settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(
                    view: WebView, url: String,
                    favicon: Bitmap?
                ) {
                    loaderVisibility.value = true
                }

                override fun onPageFinished(
                    view: WebView, url: String
                ) {
                    loaderVisibility.value = false
                }
            }
            webViewClient = WebViewClient()
            loadUrl(url)
        }
    })
}

// Alternatively I can open in chrome custom tabs.
fun openTab(context: Context, url: String) {
    val activity = (context as? Activity)

    val builder = CustomTabsIntent.Builder()
    builder.setShowTitle(true)
    builder.setInstantAppsEnabled(true)
    builder.setDefaultColorSchemeParams(
        CustomTabColorSchemeParams
            .Builder()
            .setToolbarColor(ContextCompat.getColor(context, R.color.blue))
            .build()
    )

    val customBuilder = builder.build()
    try {
        customBuilder.launchUrl(context, Uri.parse(url))
    } catch (e: Exception) {
        val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        activity?.startActivity(i)
    }
}