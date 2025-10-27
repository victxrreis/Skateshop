package br.com.skateshop.ui.features.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.skateshop.ui.components.ProductCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onProdutoClick: (String) -> Unit,
    onCarrinhoClick: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val produtos by viewModel.produtosExibidos.collectAsState()
    val categorias by viewModel.categorias.collectAsState()
    val categoriaSelecionada by viewModel.filtroCategoria.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Skateshop") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar para seleção")
                    }
                },
                actions = {
                    IconButton(onClick = onCarrinhoClick) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Carrinho")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            CategoriasRow(
                categorias = categorias,
                selecionada = categoriaSelecionada,
                onCategoriaClick = { categoria ->
                    viewModel.selecionarCategoria(categoria)
                }
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(produtos) { produto ->
                    ProductCard(
                        produto = produto,
                        onProdutoClick = { onProdutoClick(produto.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoriasRow(
    categorias: List<String>,
    selecionada: String?,
    onCategoriaClick: (String?) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Button(
                onClick = { onCategoriaClick(null) },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selecionada == null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (selecionada == null) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text("Todos")
            }
        }

        items(categorias) { categoria ->
            Button(
                onClick = { onCategoriaClick(categoria) },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selecionada == categoria) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (selecionada == categoria) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text(categoria.replaceFirstChar { it.titlecase() })
            }
        }
    }
}