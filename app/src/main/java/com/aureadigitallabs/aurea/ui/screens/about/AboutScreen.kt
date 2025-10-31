package com.aureadigitallabs.aurea.ui.screens.info

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aureadigitallabs.aurea.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Quiénes Somos") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.aurealogo),
                contentDescription = "Logo Áurea",
                modifier = Modifier
                    .size(150.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Áurea es un emprendimiento digital impulsado por Aurea Digital Labs. Nuestra misión es conectar a los amantes del deporte extremo con productos de calidad, accesibles y confiables.\n\n" +
                        "Promovemos el espíritu de libertad, energía y comunidad que caracteriza al skate, roller y BMX. Buscamos inspirar a cada persona a alcanzar su máximo potencial sobre ruedas.",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Visión: Ser líderes en innovación y confianza dentro del mercado de deportes extremos en Latinoamérica.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
