package br.com.skateshop.ui.features.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.skateshop.data.model.Produto // Importado
import br.com.skateshop.ui.components.InfoRow
import br.com.skateshop.ui.features.home.HomeViewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalheProdutoScreen(
    viewModel: HomeViewModel,
    produtoId: String,
    onBack: () -> Unit,
    onAddToCartClick: (Produto) -> Unit // Assinatura atualizada
) {
    val produto by viewModel.produtoSelecionado.collectAsState()

    LaunchedEffect(key1 = produtoId) {
        viewModel.getProdutoById(produtoId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhe do Produto") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Voltar")
                    }
                }
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Button(
                    // Lógica do onClick atualizada
                    onClick = {
                        produto?.let { onAddToCartClick(it) }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(50.dp),
                    // Desabilita o botão enquanto o produto não for carregado
                    enabled = produto != null
                ) {
                    // Texto do botão atualizado
                    Text("Adicionar ao Carrinho", fontSize = 16.sp)
                }
            }
        }
    ) { padding ->
        if (produto == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    // Ajuste no padding para não cobrir o conteúdo com a bottomBar
                    .padding(bottom = 70.dp) 
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = produto!!.imagemResId,
                    contentDescription = produto!!.nome,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.2f),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(produto!!.nome, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text("R$ %.2f".format(produto!!.preco), style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(16.dp))

                    Text("Detalhes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(8.dp))
                    Text(produto!!.descricao, style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(16.dp))

                    InfoRow(label = "Estoque:", value = "${produto!!.estoque} unidades")
                    InfoRow(label = "Medidas:", value = produto!!.medidas)
                    InfoRow(label = "Categoria:", value = produto!!.categoria.replaceFirstChar { it.titlecase() })
                }
            }
        }
    }
}