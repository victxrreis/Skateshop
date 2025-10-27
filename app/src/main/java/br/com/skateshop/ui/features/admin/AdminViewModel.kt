package br.com.skateshop.ui.features.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.skateshop.R
import br.com.skateshop.data.model.Produto
import br.com.skateshop.data.repository.ProdutoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

enum class ModoEdicao { CRIAR, EDITAR }

data class EditProductUiState(
    val produto: Produto? = null,
    val nome: String = "",
    val descricao: String = "",
    val preco: String = "",
    val estoque: String = "",
    val categoria: String = "",
    val medidas: String = "",
    val imagemResId: Int = R.drawable.ic_launcher_background,
    val modo: ModoEdicao = ModoEdicao.CRIAR,
    val isLoading: Boolean = false,
    val erro: String? = null,
    val salvoComSucesso: Boolean = false,
    val podeSalvar: Boolean = false
) {
    constructor(produto: Produto) : this(
        produto = produto,
        nome = produto.nome,
        descricao = produto.descricao,
        preco = produto.preco.toString(),
        estoque = produto.estoque.toString(),
        categoria = produto.categoria,
        medidas = produto.medidas,
        imagemResId = produto.imagemResId,
        modo = ModoEdicao.EDITAR
    )
}


class AdminViewModel(private val repository: ProdutoRepository) : ViewModel() {

    val produtos: StateFlow<List<Produto>> = repository.todosProdutos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    private val _editProductUiState = MutableStateFlow(EditProductUiState())
    val editProductUiState: StateFlow<EditProductUiState> = _editProductUiState.asStateFlow()

    private val _categorias = MutableStateFlow<List<String>>(emptyList())
    val categorias: StateFlow<List<String>> = _categorias.asStateFlow()

    val imagensDisponiveis = listOf(
        R.drawable.shape_element, R.drawable.shape_santa_cruz, R.drawable.roda_spitfire_52mm,
        R.drawable.roda_bones_54mm, R.drawable.truck_independent, R.drawable.camiseta_thrasher,
        R.drawable.rolamento_bones_ceramic, R.drawable.ic_launcher_background // Inclui um default
    )

    init {
        _categorias.value = repository.getCategorias()
    }

    fun iniciarNovoProduto() {
        _editProductUiState.value = EditProductUiState(modo = ModoEdicao.CRIAR, salvoComSucesso = false)
    }

    fun carregarProdutoParaEdicao(produto: Produto) {
        _editProductUiState.value = EditProductUiState(produto).copy(salvoComSucesso = false)
        validarCampos()
    }

    fun resetarStatusSalvo() {
        _editProductUiState.value = _editProductUiState.value.copy(salvoComSucesso = false)
    }

    fun atualizarCampoNome(valor: String) {
        _editProductUiState.value = _editProductUiState.value.copy(nome = valor, salvoComSucesso = false)
        validarCampos()
    }
    fun atualizarCampoDescricao(valor: String) {
        _editProductUiState.value = _editProductUiState.value.copy(descricao = valor, salvoComSucesso = false)
        validarCampos()
    }
    fun atualizarCampoPreco(valor: String) {
        _editProductUiState.value = _editProductUiState.value.copy(preco = valor, salvoComSucesso = false)
        validarCampos()
    }
    fun atualizarCampoEstoque(valor: String) {
        _editProductUiState.value = _editProductUiState.value.copy(estoque = valor, salvoComSucesso = false)
        validarCampos()
    }
    fun atualizarCampoCategoria(valor: String) {
        _editProductUiState.value = _editProductUiState.value.copy(categoria = valor, salvoComSucesso = false)
        validarCampos()
    }
    fun atualizarCampoMedidas(valor: String) {
        _editProductUiState.value = _editProductUiState.value.copy(medidas = valor, salvoComSucesso = false)
        validarCampos()
    }
    fun atualizarImagem(resId: Int) {
        _editProductUiState.value = _editProductUiState.value.copy(imagemResId = resId, salvoComSucesso = false)
        validarCampos()
    }

    private fun validarCampos() {
        val state = _editProductUiState.value
        val nomeOk = state.nome.isNotBlank()
        val precoOk = state.preco.toDoubleOrNull() != null && state.preco.toDouble() >= 0
        val estoqueOk = state.estoque.toIntOrNull() != null && state.estoque.toInt() >= 0
        val categoriaOk = state.categoria.isNotBlank() && categorias.value.contains(state.categoria)

        _editProductUiState.value = state.copy(podeSalvar = nomeOk && precoOk && estoqueOk && categoriaOk)
    }

    fun salvarProduto() {
        val state = _editProductUiState.value
        if (!state.podeSalvar) return

        val preco = state.preco.toDoubleOrNull() ?: 0.0
        val estoque = state.estoque.toIntOrNull() ?: 0

        viewModelScope.launch {
            _editProductUiState.value = state.copy(isLoading = true, erro = null, salvoComSucesso = false)
            try {
                if (state.modo == ModoEdicao.CRIAR) {
                    val novoProduto = Produto(
                        id = UUID.randomUUID().toString(),
                        nome = state.nome.trim(),
                        descricao = state.descricao.trim(),
                        preco = preco,
                        imagemResId = state.imagemResId,
                        estoque = estoque,
                        categoria = state.categoria,
                        medidas = state.medidas.trim()
                    )
                    repository.insertProduto(novoProduto)
                } else {
                    state.produto?.let { produtoBase ->
                        val produtoAtualizado = produtoBase.copy(
                            nome = state.nome.trim(),
                            descricao = state.descricao.trim(),
                            preco = preco,
                            imagemResId = state.imagemResId,
                            estoque = estoque,
                            categoria = state.categoria,
                            medidas = state.medidas.trim()
                        )
                        repository.updateProduto(produtoAtualizado)
                    } ?: run {
                        throw IllegalStateException("Tentativa de editar produto nulo")
                    }
                }
                _editProductUiState.value = _editProductUiState.value.copy(isLoading = false, salvoComSucesso = true)
            } catch (e: Exception) {
                _editProductUiState.value = _editProductUiState.value.copy(isLoading = false, erro = "Erro ao salvar: ${e.message}", salvoComSucesso = false)
            }
        }
    }

    fun deletarProduto(produto: Produto) {
        viewModelScope.launch {
            try {
                repository.deleteProduto(produto)
            } catch (e: Exception) {
                println("Erro ao deletar produto: ${e.message}")
                _editProductUiState.value = _editProductUiState.value.copy(erro = "Erro ao deletar: ${e.message}") // Exemplo
            }
        }
    }
}