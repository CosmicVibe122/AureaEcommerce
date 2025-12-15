package com.aureadigitallabs.aurea.ui.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.aureadigitallabs.aurea.AureaApplication
import com.aureadigitallabs.aurea.R
import com.aureadigitallabs.aurea.model.Category
import com.aureadigitallabs.aurea.model.Product
import com.aureadigitallabs.aurea.ui.common.getCategoryIcon
import com.aureadigitallabs.aurea.ui.drawer.AppDrawerContent
import com.aureadigitallabs.aurea.ui.navigation.NavRoutes
import com.aureadigitallabs.aurea.viewmodel.CatalogViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageCarousel() {
    val images = listOf(
        painterResource(id = R.drawable.ciclismo),
        painterResource(id = R.drawable.paracadaismo),
        painterResource(id = R.drawable.skateboarding)
    )
    val pagerState = rememberPagerState(pageCount = { images.size })

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
            pagerState.animateScrollToPage(nextPage)
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) { page ->
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Image(
                painter = images[page],
                contentDescription = "Promoción ${page + 1}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryButton(category: Category, icon: ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier.padding(4.dp).aspectRatio(1f).clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = category.name, modifier = Modifier.size(36.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun FeaturedProductCard(product: Product, onClick: () -> Unit) {
    val currencyFormat = remember {
        val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
        format.maximumFractionDigits = 0
        format
    }

    val context = LocalContext.current
    val imageResId = remember(product.imageName) {
        context.resources.getIdentifier(product.imageName, "drawable", context.packageName)
    }

    Card(
        modifier = Modifier
            .width(180.dp)
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Image(
                painter = if (imageResId != 0) painterResource(id = imageResId) else painterResource(id = R.drawable.aurealogo),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = currencyFormat.format(product.price),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    showPurchaseMessage: Boolean,
    role: String?
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val application = context.applicationContext as AureaApplication
    val catalogViewModel: CatalogViewModel = viewModel(
        factory = CatalogViewModel.Factory(application.productRepository)
    )
    val categories by catalogViewModel.categories.collectAsState(initial = emptyList())
    val allProducts by catalogViewModel.allProducts.collectAsState(initial = emptyList())

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(showPurchaseMessage) {
        if (showPurchaseMessage) {
            snackbarHostState.showSnackbar("¡Gracias por tu compra!")
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(navController = navController, role = role) {
                scope.launch {
                    drawerState.close()
                }
            }
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = { Text("Aurea - Rol: ${role ?: "ninguno"}") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Abrir menú")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    item {
                        Box(modifier = Modifier.padding(16.dp)) {
                            ImageCarousel()
                        }
                    }

                    item {
                        Text(
                            "Categorías",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)
                        )
                    }

                    // Reemplazamos LazyVerticalGrid con una implementación dentro de LazyColumn
                    items(categories.chunked(3)) { rowCategories ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            rowCategories.forEach { category ->
                                Box(modifier = Modifier.weight(1f)) {
                                    CategoryButton(
                                        category = category,
                                        icon = getCategoryIcon(category.iconName)
                                    ) {
                                        navController.navigate("${NavRoutes.Catalog.route}?category=${category.name}")
                                    }
                                }
                            }
                            // Rellenar con spacers si la fila no está completa
                            repeat(3 - rowCategories.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    item {
                        Text(
                            "Productos Destacados",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                    item {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 8.dp),
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            items(allProducts.take(4)) { product ->
                                FeaturedProductCard(product = product) {
                                    navController.navigate("productDetail/${product.id}")
                                }
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp)) // Espacio al final del scroll
                    }
                }

                // Botones fijos en la parte inferior
                Divider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = { navController.navigate("${NavRoutes.Catalog.route}?category=") },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Store, contentDescription = "Catálogo")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Catálogo")
                        }
                        OutlinedButton(
                            onClick = { navController.navigate("${NavRoutes.Cart.route}?role=${role ?: "user"}") },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Carrito")
                        }
                    }
                    OutlinedButton(
                        onClick = {
                            navController.navigate(NavRoutes.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar Sesión")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cerrar Sesión")
                    }
                }
            }
        }
    }
}
