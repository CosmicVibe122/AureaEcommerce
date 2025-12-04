package com.aureadigitallabs.aurea.ui.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
fun CategoryButton(category: Category, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.name,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 2.dp)
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
    val categories = Category.values().toList()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val application = context.applicationContext as AureaApplication
    val catalogViewModel: CatalogViewModel = viewModel(
        factory = CatalogViewModel.Factory(application.productRepository)
    )
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
                Box(modifier = Modifier.padding(16.dp)) {
                    ImageCarousel()
                }

                Text(
                    "Categorías",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                ) {
                    items(categories) { category ->
                        CategoryButton(category = category) {
                            navController.navigate("${NavRoutes.Catalog.route}?category=${category.name}")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Productos Destacados",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

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

                Spacer(modifier = Modifier.weight(1f))
                Divider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(onClick = { navController.navigate("${NavRoutes.Catalog.route}?category=") }) {
                        Icon(Icons.Default.Store, contentDescription = "Catálogo")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Catálogo")
                    }
                    Button(onClick = { navController.navigate(NavRoutes.Cart.route) }) {
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
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
