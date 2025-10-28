package br.com.skateshop.ui.features.selection

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SelectionScreen(
    onClienteClick: () -> Unit, // Função que navega para a Home/Catálogo
    onAdminClick: () -> Unit    // Função que navega para o Login do Admin
) {
    // Column organiza os itens verticalmente (em coluna)
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally, // Centraliza os itens na horizontal
        verticalArrangement = Arrangement.Center // Centraliza o grupo de itens na vertical
    ) {
        // 1. Título "SKATESHOP"
        Text(
            text = "SKATESHOP",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 64.dp) // Espaço abaixo do título
        )

        Spacer(modifier = Modifier.height(32.dp)) // Espaçamento extra

        // 2. Botão "ENTRAR (Catálogo)"
        Button(
            // Ao clicar, chama a função de navegação configurada no AppNavigation
            onClick = onClienteClick,
            modifier = Modifier
                .fillMaxWidth(0.6f) // Ocupa 60% da largura
                .padding(bottom = 16.dp)
        ) {
            Text("ENTRAR")
        }

        // 3. Botão "ACESSO ADMIN (Login)"
        Button(
            // Ao clicar, chama a função de navegação configurada no AppNavigation
            onClick = onAdminClick,
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text("ACESSO ADMIN")
        }
    }
}