package com.compose.quotesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.compose.quotesapp.model.QuoteManager
import com.compose.quotesapp.screens.QuoteDetail
import com.compose.quotesapp.screens.QuoteListScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        CoroutineScope(Dispatchers.IO).launch {
            delay(5000)
            QuoteManager.loadAssetFromFile(applicationContext)
        }

        setContent {
            App()
        }
    }
}

@Composable
fun App() {
    if (QuoteManager.isDataLoaded.value) {
        if (QuoteManager.currentPage.value == Pages.LISTING) {
            QuoteListScreen(data = QuoteManager.data) {
                QuoteManager.switchPages(it)
            }
        } else {
            QuoteManager.currentQuote?.let { QuoteDetail(quote = it) }
        }
    } else {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(1f)
        ) {
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}

enum class Pages {
    LISTING,
    DETAIL
}