package com.pemmob.nailaalifatul.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pemmob.nailaalifatul.R
import com.pemmob.nailaalifatul.data.dummy.DummyData
import com.pemmob.nailaalifatul.data.model.Category
import com.pemmob.nailaalifatul.data.model.Product
import kotlinx.coroutines.delay

private val MainGreenColor = Color(0xFF4CAF50)

@Composable
fun CategoryItem(category: Category, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) MainGreenColor else Color(0xFFF0EBF5)
    val textColor = if (isSelected) Color.White else Color.Black

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = category.name,
            color = textColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ProductItemCard(product: Product, onClick: () -> Unit) {
    val categoryName = product.category?.name ?: DummyData.categories.find { it.id == product.category_id }?.name ?: "Kategori"

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .background(Color(0xFFE0E0E0), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = product.img), 
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp))
                    )
                    
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = MainGreenColor,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = categoryName,
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Rp ${product.price}",
                    color = MainGreenColor,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarProdukScreen(navController: NavController? = null) {
    var selectedCategoryId by rememberSaveable { mutableStateOf(DummyData.categories.firstOrNull()?.id) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var filteredProducts by remember { mutableStateOf(emptyList<Product>()) }

    LaunchedEffect(key1 = selectedCategoryId, key2 = searchQuery) {
        isLoading = true
        delay(1000)
        val allProducts = DummyData.product

        val filteredByCategory = if (selectedCategoryId != null) {
            allProducts.filter { it.category_id == selectedCategoryId }
        } else allProducts

        filteredProducts = if (searchQuery.isBlank()) {
            filteredByCategory
        } else {
            filteredByCategory.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }
        isLoading = false
    }

    StatelessDaftarProduct(
        categories = DummyData.categories,
        selectedCategoryId = selectedCategoryId,
        onCategorySelected = { selectedCategoryId = it },
        searchQuery = searchQuery,
        onSearchQueryChange = { searchQuery = it },
        isLoading = isLoading,
        products = filteredProducts,
        onProductClick = { product ->
            navController?.navigate("detail/${product.id}")
        },
        onContactUsClick = {
            navController?.navigate("hubungi_kami")
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatelessDaftarProduct(
    categories: List<Category>,
    selectedCategoryId: Int?,
    onCategorySelected: (Int) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    isLoading: Boolean,
    products: List<Product>,
    onProductClick: (Product) -> Unit,
    onContactUsClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daftar Produk UMKM", color = Color.White) },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart", tint = Color.White)
                    }
                    var expanded by remember { mutableStateOf(false) }
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu",
                            tint = Color.White
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Hubungi Kami") },
                            onClick = {
                                expanded = false
                                onContactUsClick()
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = "Email"
                                )
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MainGreenColor)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFAFAFA))
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Cari produk...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )

            Text(
                text = "Kategori Produk",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    CategoryItem(
                        category = category,
                        isSelected = category.id == selectedCategoryId,
                        onClick = { onCategorySelected(category.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Daftar Produk",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = MainGreenColor)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Mencari data...")
                    }
                }
            } else {
                if (products.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Produk tidak ditemukan.")
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(count = 2),
                        contentPadding = PaddingValues(all = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(products) { product ->
                            ProductItemCard(product = product) {
                                onProductClick(product)
                            }
                        }
                    }
                }
            }
        }
    }
}
