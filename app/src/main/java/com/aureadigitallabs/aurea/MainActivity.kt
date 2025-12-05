package com.aureadigitallabs.aurea

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.aureadigitallabs.aurea.ui.navigation.NavGraph
import com.aureadigitallabs.aurea.ui.theme.AureaTheme
import com.aureadigitallabs.aurea.viewmodel.CartViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()

        val aureaApp = application as AureaApplication

        setContent {
            AureaTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()

                    val cartViewModel: CartViewModel = viewModel(
                        factory = CartViewModel.Factory(aureaApp.sessionManager)
                    )


                    NavGraph(navController, cartViewModel)
                }
            }
        }
    }
}