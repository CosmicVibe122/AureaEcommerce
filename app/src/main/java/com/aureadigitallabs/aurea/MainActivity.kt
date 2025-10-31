package com.aureadigitallabs.aurea

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.rememberNavController
import com.aurea.app.ui.theme.AureaTheme
import com.aureadigitallabs.aurea.ui.navigation.NavGraph

class MainActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AureaTheme{
                Surface(color = MaterialTheme.colorScheme.background){
                    val navController = rememberNavController()
                    NavGraph(navController)
                }
            }
        }
    }
}