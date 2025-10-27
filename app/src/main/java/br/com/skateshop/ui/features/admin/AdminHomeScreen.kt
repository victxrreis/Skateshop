package br.com.skateshop.ui.features.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.skateshop.data.model.Produto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(
    viewModel: AdminViewModel,
    onAddProductClick: () -> Unit,
    onEditProductClick: (Produto) -> Unit,
    onLogoutClick: () -> Unit // Adicionado botão de logout
) {
    val produtos by viewModel.produtos.collectAsState()
    var showDeleteDialog by remember { mutableStateOf<Produto?>(null) } // Guarda o produto a ser deletado

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gerenciar Produtos") },
                actions = {
                    TextButton(onClick = onLogoutClick) {
                        Text("Sair")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddProductClick) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Produto")
            }
        }
    ) { padding ->
        if (produtos.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Nenhum produto cadastrado.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(produtos) { produto ->
                    ProdutoAdminItem(
                        produto = produto,
                        onEditClick = { onEditProductClick(produto) },
                        onDeleteClick = { showDeleteDialog = produto } // Mostra o diálogo
                    )
                }
            }
        }
    }

    // Diálogo de confirmação para deletar
    if (showDeleteDialog != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Confirmar Exclusão") },
            text = { Text("Tem certeza que deseja excluir o produto '${showDeleteDialog?.nome}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deletarProduto(showDeleteDialog!!)
                        showDeleteDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun ProdutoAdminItem(
    produto: Produto,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(produto.nome, fontWeight = FontWeight.Bold)
                Text("Estoque: ${produto.estoque} | R$ %.2f".format(produto.preco))
            }
            Row {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = "Deletar", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}