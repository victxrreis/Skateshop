package br.com.skateshop.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.skateshop.ui.features.admin.AdminEditProductScreen
import br.com.skateshop.ui.features.admin.AdminHomeScreen
import br.com.skateshop.ui.features.admin.AdminLoginScreen
import br.com.skateshop.ui.features.admin.AdminViewModel
import br.com.skateshop.ui.features.admin.AdminViewModelFactory
import br.com.skateshop.ui.features.details.DetalheProdutoScreen
import br.com.skateshop.ui.features.home.HomeScreen
import br.com.skateshop.ui.features.home.HomeViewModel
import br.com.skateshop.ui.features.home.HomeViewModelFactory
import br.com.skateshop.ui.features.placeholders.PlaceholderScreen // Manter para Carrinho
import br.com.skateshop.ui.features.placeholders.TelaSelecaoModoPlaceholder

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val homeViewModelFactory = HomeViewModelFactory(context)
    val homeViewModel: HomeViewModel = viewModel(factory = homeViewModelFactory)

    val adminViewModelFactory = AdminViewModelFactory(context)
    val adminViewModel: AdminViewModel = viewModel(factory = adminViewModelFactory)

    NavHost(navController = navController, startDestination = "TELA_SELECAO_MODO") {

        composable(Routes.TELA_SELECAO_MODO) {
            TelaSelecaoModoPlaceholder(
                onClienteClick = { navController.navigate(Routes.HOME_SCREEN) },
                onAdminClick = { navController.navigate(Routes.ADMIN_LOGIN_SCREEN) }
            )
        }

        composable(Routes.HOME_SCREEN) {
            HomeScreen(
                viewModel = homeViewModel,
                onProdutoClick = { produtoId ->
                    navController.navigate("${Routes.DETALHE_PRODUTO_SCREEN}/$produtoId")
                },
                onCarrinhoClick = {
                    navController.navigate(Routes.CARRINHO_SCREEN) // Vai para Placeholder
                }
            )
        }
        composable(
            route = "${Routes.DETALHE_PRODUTO_SCREEN}/{produtoId}",
            arguments = listOf(navArgument("produtoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val produtoId = backStackEntry.arguments?.getString("produtoId") ?: "ID_INVALIDO"
            DetalheProdutoScreen(
                viewModel = homeViewModel,
                produtoId = produtoId,
                onBack = { navController.popBackStack() },
                onAddToCartClick = {
                    navController.navigate(Routes.CARRINHO_SCREEN) // Vai para Placeholder
                }
            )
        }

        composable(Routes.CARRINHO_SCREEN) {
            PlaceholderScreen("Fase 4: Carrinho", navController, Routes.CHECKOUT_SCREEN)
        }
        composable(Routes.CHECKOUT_SCREEN) {
            PlaceholderScreen("Fase 4: Checkout", navController, Routes.PEDIDO_SUCESSO_SCREEN)
        }
        composable(Routes.PEDIDO_SUCESSO_SCREEN) {
            PlaceholderScreen("Fase 4: Pedido com Sucesso", navController, Routes.HOME_SCREEN)
        }

        composable(Routes.ADMIN_LOGIN_SCREEN) {
            AdminLoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.ADMIN_HOME_SCREEN) {
                        popUpTo(Routes.ADMIN_LOGIN_SCREEN) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.ADMIN_HOME_SCREEN) {
            AdminHomeScreen(
                viewModel = adminViewModel,
                onAddProductClick = {
                    adminViewModel.iniciarNovoProduto()
                    navController.navigate(Routes.ADMIN_EDIT_PRODUCT_SCREEN)
                },
                onEditProductClick = { produto ->
                    adminViewModel.carregarProdutoParaEdicao(produto)
                    navController.navigate(Routes.ADMIN_EDIT_PRODUCT_SCREEN)
                },
                onLogoutClick = {
                    navController.navigate(Routes.TELA_SELECAO_MODO) {
                        popUpTo(Routes.ADMIN_HOME_SCREEN) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.ADMIN_EDIT_PRODUCT_SCREEN) {
            AdminEditProductScreen(
                viewModel = adminViewModel,
                onNavigateBack = { navController.popBackStack() } // Simplesmente volta
            )
        }
    }
}

object Routes {
    const val TELA_SELECAO_MODO = "tela_selecao_modo"
    const val HOME_SCREEN = "home_screen"
    const val DETALHE_PRODUTO_SCREEN = "detalhe_produto_screen"
    const val CARRINHO_SCREEN = "carrinho_screen"
    const val CHECKOUT_SCREEN = "checkout_screen"
    const val PEDIDO_SUCESSO_SCREEN = "pedido_sucesso_screen"
    const val ADMIN_LOGIN_SCREEN = "admin_login_screen"
    const val ADMIN_HOME_SCREEN = "admin_home_screen"
    const val ADMIN_EDIT_PRODUCT_SCREEN = "admin_edit_product_screen"
}