package br.com.skateshop.ui.features.cart

import androidx.lifecycle.ViewModel // <-- Corrigido de 'androidxx' para 'androidx'
import androidx.lifecycle.viewModelScope
import br.com.skateshop.data.model.Produto
import br.com.skateshop.data.repository.ProdutoRepository // <-- Certifique-se que este import está correto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

// Data class para representar um item no carrinho
data class CartItem(
    val produto: Produto,
    val quantidade: Int
)

// Estado da UI do Carrinho
data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val total: Double = 0.0
)

class CartViewModel(
    // Requer o Repository para que a Factory possa injetá-lo
    private val repository: ProdutoRepository
) : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())

    // Estado da UI que combina itens e total
    val uiState: StateFlow<CartUiState> = _cartItems.map { items ->
        val total = items.sumOf { it.produto.preco * it.quantidade }
        CartUiState(items = items, total = total)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = CartUiState()
    )

    fun adicionarItem(produto: Produto) {
        _cartItems.update { currentItems ->
            val existingItem = currentItems.find { it.produto.id == produto.id }
            if (existingItem != null) {
                // Se o item já existe, atualiza a quantidade
                currentItems.map {
                    if (it.produto.id == produto.id) {
                        it.copy(quantidade = it.quantidade + 1)
                    } else {
                        it
                    }
                }
            } else {
                // Se é um novo item, adiciona à lista
                currentItems + CartItem(produto = produto, quantidade = 1)
            }
        }
    }

    fun removerItem(produtoId: String) {
        _cartItems.update { currentItems ->
            currentItems.filterNot { it.produto.id == produtoId }
        }
    }

    fun alterarQuantidade(produtoId: String, novaQuantidade: Int) {
        if (novaQuantidade <= 0) {
            removerItem(produtoId)
            return
        }

        _cartItems.update { currentItems ->
            currentItems.map {
                if (it.produto.id == produtoId) {
                    it.copy(quantidade = novaQuantidade)
                } else {
                    it
                }
            }
        }
    }

    // Limpa o carrinho após o pedido (será usado pelo CheckoutViewModel)
    fun limparCarrinho() {
        _cartItems.value = emptyList()
    }
}