package com.aureadigitallabs.aurea.ui.screens.login
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.aureadigitallabs.aurea.R
// ---

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.aureadigitallabs.aurea.model.UserRole
import com.aureadigitallabs.aurea.ui.navigation.NavRoutes
import com.aureadigitallabs.aurea.viewmodel.LoginViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = viewModel()) {

    val username by viewModel.username
    val password by viewModel.password
    val errorMessage by viewModel.errorMessage
    val loggedUser by viewModel.loggedUser

    // Si el usuario se logueó correctamente, navega al Home
    if (loggedUser != null) {
        val role = loggedUser!!.role.name
        // Pasamos el rol a la pantalla Home
        LaunchedEffect(Unit) {
            navController.navigate("${NavRoutes.Home.route}/$role") {
                popUpTo(NavRoutes.Login.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- CÓDIGO DEL LOGO AÑADIDO AQUÍ ---
        Image(
            painter = painterResource(id = R.drawable.aurealogo),
            contentDescription = stringResource(R.string.aurea_logo_description),
            modifier = Modifier
                .size(200.dp) // Puedes ajustar el tamaño si lo deseas
                .padding(bottom = 32.dp) // Aumentamos el espacio inferior para separarlo del título
        )
        // ------------------------------------

        Text("Inicio de Sesión", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { viewModel.username.value = it },
            label = { Text("Usuario") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { viewModel.password.value = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.login() }) {
            Text("Ingresar")
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
        }
    }
}
