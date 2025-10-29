package br.com.skateshop.ui.features.checkout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.skateshop.ui.features.cart.CartUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    viewModel: CheckoutViewModel, // O MESMO ViewModel
    cartState: CartUiState, // Recebe o estado do carrinho para mostrar o total
    onBack: () -> Unit,
    onOrderSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Calcula o total final
    val totalProdutos = cartState.total
    val valorFrete = uiState.opcaoFrete?.valor ?: 0.0
    val totalFinal = totalProdutos + valorFrete
    
    // Validação simples
    val podePagar = uiState.numeroCartao.isNotBlank() &&
                    uiState.validadeCartao.isNotBlank() &&
                    uiState.cvvCartao.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pagamento") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        "Total: R$ %.2f".format(totalFinal),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(4.dp))
                    Button(
                        onClick = onOrderSuccess,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        enabled = podePagar
                    ) {
                        Text("Pagar e Finalizar Pedido", fontSize = 16.sp)
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Pagamento com Cartão", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.numeroCartao,
                onValueChange = viewModel::onNumeroCartaoChange,
                label = { Text("Número do Cartão") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = uiState.validadeCartao,
                    onValueChange = viewModel::onValidadeCartaoChange,
                    label = { Text("Validade (MM/AA)") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = uiState.cvvCartao,
                    onValueChange = viewModel::onCvvCartaoChange,
                    label = { Text("CVV") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            
            Spacer(Modifier.height(24.dp))
            Text("Resumo do Pedido", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            InfoRow(label = "Produtos:", value = "R$ %.2f".format(totalProdutos))
            InfoRow(label = "Frete:", value = "R$ %.2f".format(valorFrete))
            Divider(Modifier.padding(vertical = 8.dp))
            InfoRow(
                label = "Total a Pagar:",
                value = "R$ %.2f".format(totalFinal),
                isBold = true
            )
        }
    }
}

// Componente auxiliar simples para o resumo (pode ir para ui/components)
@Composable
fun InfoRow(label: String, value: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            value,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
    }
}