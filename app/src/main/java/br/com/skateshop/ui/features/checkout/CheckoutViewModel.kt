package br.com.skateshop.ui.features.checkout

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Enum para as opções de frete
enum class OpcaoFrete(val descricao: String, val valor: Double) {
    PAC("PAC (7-10 dias)", 15.00),
    SEDEX("SEDEX (2-4 dias)", 30.00)
}

// Estado da UI para todo o fluxo de checkout
data class CheckoutUiState(
    // Contato
    val nome: String = "",
    val email: String = "",
    val telefone: String = "",
    // Endereço
    val cep: String = "",
    val rua: String = "",
    val numero: String = "",
    val bairro: String = "",
    val cidade: String = "",
    // Frete
    val opcaoFrete: OpcaoFrete? = null,
    // Pagamento
    val numeroCartao: String = "",
    val validadeCartao: String = "",
    val cvvCartao: String = ""
)

class CheckoutViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState = _uiState.asStateFlow()

    // --- Métodos para Contato/Endereço (CheckoutScreen) ---
    fun onNomeChange(valor: String) {
        _uiState.update { it.copy(nome = valor) }
    }
    fun onEmailChange(valor: String) {
        _uiState.update { it.copy(email = valor) }
    }
    fun onTelefoneChange(valor: String) {
        _uiState.update { it.copy(telefone = valor) }
    }
    fun onCepChange(valor: String) {
        _uiState.update { it.copy(cep = valor) }
    }
    fun onRuaChange(valor: String) {
        _uiState.update { it.copy(rua = valor) }
    }
    fun onNumeroChange(valor: String) {
        _uiState.update { it.copy(numero = valor) }
    }
    fun onBairroChange(valor: String) {
        _uiState.update { it.copy(bairro = valor) }
    }
    fun onCidadeChange(valor: String) {
        _uiState.update { it.copy(cidade = valor) }
    }
    fun onFreteChange(opcao: OpcaoFrete) {
        _uiState.update { it.copy(opcaoFrete = opcao) }
    }
    
    // --- Métodos para Pagamento (PaymentScreen) ---
    fun onNumeroCartaoChange(valor: String) {
        _uiState.update { it.copy(numeroCartao = valor) }
    }
    fun onValidadeCartaoChange(valor: String) {
        _uiState.update { it.copy(validadeCartao = valor) }
    }
    fun onCvvCartaoChange(valor: String) {
        _uiState.update { it.copy(cvvCartao = valor) }
    }

    // Limpa o estado após o pedido
    fun limparCheckout() {
        _uiState.value = CheckoutUiState()
    }
}