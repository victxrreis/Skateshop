package br.com.skateshop.ui.features.checkout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    viewModel: CheckoutViewModel, // Recebe o VM
    onBack: () -> Unit,
    onGoToPayment: () -> Unit // Nova ação de navegação
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    
    // Validação simples (apenas para habilitar o botão)
    val podeAvancar = uiState.nome.isNotBlank() && uiState.email.isNotBlank() &&
                      uiState.cep.isNotBlank() && uiState.rua.isNotBlank() &&
                      uiState.numero.isNotBlank() && uiState.bairro.isNotBlank() &&
                      uiState.cidade.isNotBlank() && uiState.opcaoFrete != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Endereço e Frete") },
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
                Button(
                    onClick = onGoToPayment, // Ação para ir ao pagamento
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(50.dp),
                    enabled = podeAvancar // Habilita o botão
                ) {
                    Text("Ir para Pagamento", fontSize = 16.sp)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            // --- Seção de Contato ---
            Text("Informações de Contato", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.nome,
                onValueChange = viewModel::onNomeChange,
                label = { Text("Nome Completo") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.telefone,
                onValueChange = viewModel::onTelefoneChange,
                label = { Text("Telefone") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            // --- Seção de Endereço ---
            Spacer(Modifier.height(24.dp))
            Text("Endereço de Entrega", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.cep,
                onValueChange = viewModel::onCepChange,
                label = { Text("CEP") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.rua,
                onValueChange = viewModel::onRuaChange,
                label = { Text("Rua / Avenida") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = uiState.numero,
                    onValueChange = viewModel::onNumeroChange,
                    label = { Text("Número") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = uiState.bairro,
                    onValueChange = viewModel::onBairroChange,
                    label = { Text("Bairro") },
                    modifier = Modifier.weight(2f)
                )
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.cidade,
                onValueChange = viewModel::onCidadeChange,
                label = { Text("Cidade") },
                modifier = Modifier.fillMaxWidth()
            )

            // --- Seção de Frete ---
            Spacer(Modifier.height(24.dp))
            Text("Opções de Frete", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            
            OpcaoFrete.values().forEach { opcao ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (uiState.opcaoFrete == opcao),
                            onClick = { viewModel.onFreteChange(opcao) },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (uiState.opcaoFrete == opcao),
                        onClick = null // O clique é tratado pelo Row
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "${opcao.descricao} - R$ %.2f".format(opcao.valor),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}