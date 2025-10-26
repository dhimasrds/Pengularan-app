package com.dhimas.pengeluaranapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.dhimas.pengeluaranapp.di.appModules
import com.dhimas.pengeluaranapp.util.AndroidContextHolder
import org.koin.core.context.startKoin
import org.koin.core.context.GlobalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        AndroidContextHolder.init(this)

        // Initialize Koin only if not already started
        if (GlobalContext.getOrNull() == null) {
            startKoin {
                modules(appModules)
            }
        }

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}