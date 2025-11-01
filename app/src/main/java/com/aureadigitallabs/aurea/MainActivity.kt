package com.aureadigitallabs.aurea

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.aureadigitallabs.aurea.ui.theme.AureaTheme
import com.aureadigitallabs.aurea.ui.navigation.NavGraph
import com.aureadigitallabs.aurea.viewmodel.CartViewModel

class MainActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AureaTheme{
                Surface(color = MaterialTheme.colorScheme.background){
                    val navController = rememberNavController()
                    // Se crea la instancia del ViewModel que se compartirá
                    val cartViewModel: CartViewModel = viewModel()
                    // Se pasa el ViewModel al NavGraph
                    NavGraph(navController, cartViewModel)
                }
            }
        }
    }
}
