package com.pemmob.nailaalifatul.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pemmob.nailaalifatul.R
import com.pemmob.nailaalifatul.data.model.Product
import com.pemmob.nailaalifatul.util.JualanConstants
import com.pemmob.nailaalifatul.viewmodel.ProductUiState
import com.pemmob.nailaalifatul.viewmodel.ProductViewModel

private val MainGreenColor = Color(0xFF4CAF50)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailProductScreen(
    productId: Int,
    navController: NavController? = null,
    viewModel: ProductViewModel
) {
    val context = LocalContext.current
    var quantity by rememberSaveable { mutableStateOf(1) }
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is ProductUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is ProductUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error)
            }
        }
        is ProductUiState.Success -> {
            val product = state.products.find { it.id == productId }

            if (product == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Produk tidak ditemukan")
                }
            } else {
                StatelessDetailProduct(
                    product = product,
                    quantity = quantity,
                    onQuantityChange = { newQuantity ->
                        quantity = newQuantity
                    },
                    onBackClick = { navController?.popBackStack() },
                    onAddToCartClick = {
                        Toast.makeText(context, "Berhasil menambah $quantity", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatelessDetailProduct(
    product: Product,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    onBackClick: () -> Unit,
    onAddToCartClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Produk") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.back_icon_svg),
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(horizontal = 32.dp, vertical = 16.dp)
                    .background(Color(0xFFCECECE), RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                val imageModel: Any = if (product.img == "dummy_product") {
                    R.drawable.icon_app_jualan
                } else {
                    "${JualanConstants.BASE_URL}img/${product.img}"
                }

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AsyncImage(
                        model = imageModel,
                        contentDescription = product.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(shape = RoundedCornerShape(size = 8.dp))
                            .background(color = androidx.compose.ui.graphics.Color.White),
                        contentScale = ContentScale.Fit
                    )
                }
            }
            
            Column(modifier = Modifier.padding(16.dp)) {
                val categoryName = product.category?.name ?: "Kategori"
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFF0EBF5),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(
                        text = categoryName,
                        color = Color(0xFF6B4B9A), // Based on the purple badge
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                
                Text(
                    product.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Rp ${product.price}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MainGreenColor
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Deskripsi", fontWeight = FontWeight.Bold)
                Text(product.description ?: "Tidak ada kolom deskripsi di JSON produk saat ini, tapi biarkan ini sebagai fallback.")
                
                Spacer(modifier = Modifier.height(8.dp))
                Text("Stok: ${product.stock}")

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Jumlah Beli")
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        FilledTonalIconButton(
                            onClick = {
                                if (quantity > 1)
                                    onQuantityChange(quantity - 1)
                            },
                            enabled = quantity > 1
                        ) {
                            Text("-")
                        }

                        Text(
                            quantity.toString(),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        FilledTonalIconButton(
                            onClick = {
                                if (quantity < product.stock)
                                    onQuantityChange(quantity + 1)
                            },
                            enabled = quantity < product.stock
                        ) {
                            Text("+")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onAddToCartClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = product.stock > 0 && quantity > 0,
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MainGreenColor)
                ) {
                    Text("Tambah ke Keranjang", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
