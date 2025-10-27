package br.com.skateshop.ui.features.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import br.com.skateshop.R
import br.com.skateshop.ui.components.InfoRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminEditProductScreen(
    viewModel: AdminViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.editProductUiState.collectAsState()
    val categorias by viewModel.categorias.collectAsState()
    val scrollState = rememberScrollState()
    var showImagePicker by remember { mutableStateOf(false) }
    var showCategoryPicker by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.salvoComSucesso) {
        if (uiState.salvoComSucesso) {
            onNavigateBack()
            viewModel.resetarStatusSalvo()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (uiState.modo == ModoEdicao.CRIAR) "Adicionar Produto" else "Editar Produto") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            OutlinedTextField(
                value = uiState.nome,
                onValueChange = { viewModel.atualizarCampoNome(it) },
                label = { Text("Nome do Produto*") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.descricao,
                onValueChange = { viewModel.atualizarCampoDescricao(it) },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth().height(100.dp)
            )
            Spacer(Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = uiState.preco,
                    onValueChange = { viewModel.atualizarCampoPreco(it.filter { char -> char.isDigit() || char == '.' }) },
                    label = { Text("Preço*") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    prefix = { Text("R$ ") }
                )
                OutlinedTextField(
                    value = uiState.estoque,
                    onValueChange = { viewModel.atualizarCampoEstoque(it.filter { char -> char.isDigit() }) },
                    label = { Text("Estoque*") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
            Spacer(Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Dropdown REAL para Categoria
                ExposedDropdownMenuBox(
                    expanded = showCategoryPicker,
                    onExpandedChange = { showCategoryPicker = !showCategoryPicker },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = uiState.categoria.ifBlank { "Selecione..." },
                        onValueChange = {},
                        label = { Text("Categoria*") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCategoryPicker) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = showCategoryPicker,
                        onDismissRequest = { showCategoryPicker = false }
                    ) {
                        categorias.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption.replaceFirstChar { it.titlecase() }) },
                                onClick = {
                                    viewModel.atualizarCampoCategoria(selectionOption)
                                    showCategoryPicker = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = uiState.medidas,
                    onValueChange = { viewModel.atualizarCampoMedidas(it) },
                    label = { Text("Medidas") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }
            Spacer(Modifier.height(16.dp))

            Text("Imagem:")
            Spacer(Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
                    .clickable { showImagePicker = true }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = uiState.imagemResId),
                        contentDescription = "Imagem selecionada",
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = if (uiState.imagemResId == R.drawable.ic_launcher_background)
                            "Escolher imagem..."
                        else
                            "Trocar imagem...",
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            }
            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.salvarProduto()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.podeSalvar && !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    Text("Salvar Produto")
                }
            }
            if (uiState.erro != null) {
                Spacer(Modifier.height(8.dp))
                Text(uiState.erro!!, color = MaterialTheme.colorScheme.error)
            }
        }

        if (showImagePicker) {
            AlertDialog(
                onDismissRequest = { showImagePicker = false },
                title = { Text("Escolher Imagem") },
                text = {
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    ) {
                        viewModel.imagensDisponiveis.forEach { resId ->
                            Image(
                                painter = painterResource(id = resId),
                                contentDescription = "Imagem $resId",
                                modifier = Modifier
                                    .size(60.dp)
                                    .padding(4.dp)
                                    .clickable {
                                        viewModel.atualizarImagem(resId)
                                        showImagePicker = false
                                    }
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showImagePicker = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}