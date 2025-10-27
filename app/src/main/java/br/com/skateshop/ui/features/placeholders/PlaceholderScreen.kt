package br.com.skateshop.ui.features.placeholders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun PlaceholderScreen(screenName: String, navController: NavHostController, nextScreenRoute: String? = null) {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(screenName, style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
            Spacer(Modifier.height(16.dp))
            Text("Esta tela será implementada em uma fase futura.", textAlign = TextAlign.Center)
            if (nextScreenRoute != null) {
                Spacer(Modifier.height(16.dp))
                Button(onClick = { navController.navigate(nextScreenRoute) }) {
                    Text("Simular: Ir para ${nextScreenRoute.split("/")[0]}")
                }
            }
            if (navController.previousBackStackEntry != null) {
                Spacer(Modifier.height(16.dp))
                Button(onClick = { navController.popBackStack() }) { Text("Voltar") }
            }
        }
    }
}

@Composable
fun TelaSelecaoModoPlaceholder(onClienteClick: () -> Unit, onAdminClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Fase 2: Tela de Seleção", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(24.dp))
            Button(onClick = onClienteClick, modifier = Modifier.padding(8.dp)) { Text("Entrar (Ir para Fase 3)") }
            Button(onClick = onAdminClick, modifier = Modifier.padding(8.dp)) { Text("Acesso (Ir para Fase 5)") }
        }
    }
}