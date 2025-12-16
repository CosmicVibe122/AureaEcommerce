package com.aureadigitallabs.aurea.ui.screens.catalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.aureadigitallabs.aurea.AureaApplication
import com.aureadigitallabs.aurea.R
import com.aureadigitallabs.aurea.ui.common.AppTopBar
import com.aureadigitallabs.aurea.viewmodel.CartViewModel
import com.aureadigitallabs.aurea.viewmodel.ProductDetailViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import coil.compose.AsyncImage
import coil.request.ImageRequest



@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: Long,
    cartViewModel: CartViewModel
) {
    val context = LocalContext.current
    val application = context.applicationContext as AureaApplication


    val viewModel: ProductDetailViewModel = viewModel(
        factory = ProductDetailViewModel.Factory(application.productRepository, productId)
    )

    val product by viewModel.product.collectAsState(initial = null)

    val imageModel: Any? = remember(product) {
        product?.let { p ->
            val isUrl = p.imageName.startsWith("http", ignoreCase = true)
            if (isUrl) {
                p.imageName // Si es URL, la usamos directamente
            } else {
                // Si no es URL, intentamos resolver el ID de recurso local (comportamiento actual)
                val resId = context.resources.getIdentifier(
                    p.imageName,
                    "drawable",
                    context.packageName
                )
                if (resId != 0) resId else p.imageName // Usamos el ID si se encuentra, o la cadena original
            }
        }
    }

    val currencyFormat = remember {
        val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
        format.maximumFractionDigits = 0
        format
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            AppTopBar(
                title = product?.name ?: "Cargando...",
                navController = navController,
                canNavigateBack = true
            )
        }
    ) { paddingValues ->
        if (product == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(imageModel) // Usa la URL o el ID de recurso
                            .crossfade(true)
                            .build(),
                        contentDescription = product!!.name,
                        placeholder = painterResource(id = R.drawable.aurealogo), // Imagen de carga
                        error = painterResource(id = R.drawable.aurealogo), // Imagen por defecto si falla la URL o el ID no se resolvió
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = product!!.name,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = currencyFormat.format(product!!.price),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = product!!.description,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        cartViewModel.addProduct(product!!)
                        scope.launch {
                            snackbarHostState.showSnackbar("¡Producto añadido al carrito!")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddShoppingCart,
                        contentDescription = "Añadir al carrito",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Añadir al Carrito", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}