package br.com.skateshop.ui.features.checkout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun OrderSuccessScreen(
    onContinueShopping: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("TODO: Implementar Tela de Pedido com Sucesso (Fase 4)")
        // Mensagem de sucesso e botão "Continuar Comprando" serão adicionados aqui
    }
}