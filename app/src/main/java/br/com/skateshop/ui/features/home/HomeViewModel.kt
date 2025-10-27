package br.com.skateshop.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.skateshop.data.model.Produto
import br.com.skateshop.data.repository.ProdutoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: ProdutoRepository) : ViewModel() {

    private val _filtroCategoria = MutableStateFlow<String?>(null)
    val filtroCategoria: StateFlow<String?> = _filtroCategoria.asStateFlow()

    private val _produtoSelecionado = MutableStateFlow<Produto?>(null)
    val produtoSelecionado: StateFlow<Produto?> = _produtoSelecionado.asStateFlow()

    private val _categorias = MutableStateFlow<List<String>>(emptyList())
    val categorias: StateFlow<List<String>> = _categorias.asStateFlow()

    val produtosExibidos: StateFlow<List<Produto>> =
        combine(repository.todosProdutos, _filtroCategoria) { produtos, categoria ->
            if (categoria == null) {
                produtos
            } else {
                produtos.filter { it.categoria == categoria }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    init {
        _categorias.value = repository.getCategorias()
    }

    fun selecionarCategoria(categoria: String?) {
        _filtroCategoria.value = categoria
    }

    fun getProdutoById(id: String) {
        viewModelScope.launch {
            repository.getProdutoPorId(id).collect { produto ->
                _produtoSelecionado.value = produto
            }
        }
    }
}