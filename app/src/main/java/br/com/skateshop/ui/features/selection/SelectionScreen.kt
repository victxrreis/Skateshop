package br.com.skateshop.ui.features.selection

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SelectionScreen(
    onClienteClick: () -> Unit,
    onAdminClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("TODO: Implementar Tela de Seleção (Fase 2)")
        // Os botões reais ("Entrar na Loja", "Acesso Administrador")
        // serão adicionados aqui pelos outros desenvolvedores.
    }
}