package com.aureadigitallabs.aurea.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.aureadigitallabs.aurea.AureaApplication
import com.aureadigitallabs.aurea.R
import com.aureadigitallabs.aurea.ui.navigation.NavRoutes
import com.aureadigitallabs.aurea.viewmodel.LoginViewModel

@Composable
fun LoginScreen(navController: NavController) {
    val application = LocalContext.current.applicationContext as AureaApplication
    val viewModel: LoginViewModel = viewModel(
        factory = LoginViewModel.Factory(
            authRepository = application.authRepository,
            sessionManager = application.sessionManager
        )
    )

    val username by viewModel.username
    val password by viewModel.password
    val errorMessage by viewModel.errorMessage
    val loginResponse by viewModel.loginResponse

    loginResponse?.let { response ->
        LaunchedEffect(response) {

            val role = response.user.role.name

            navController.navigate("${NavRoutes.Home.route}/$role?showPurchaseMessage=false") {
                popUpTo(NavRoutes.Login.route) { inclusive = true }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .imePadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.aurealogo),
            contentDescription = stringResource(R.string.aurea_logo_description),
            modifier = Modifier.size(200.dp).padding(bottom = 32.dp)
        )

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

        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
    }
}