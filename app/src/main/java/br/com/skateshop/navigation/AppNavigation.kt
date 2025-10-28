package br.com.skateshop.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.skateshop.ui.features.admin.AdminViewModelFactory
import br.com.skateshop.ui.features.home.HomeViewModelFactory
import br.com.skateshop.ui.features.admin.AdminViewModel
import br.com.skateshop.ui.features.home.HomeViewModel
import br.com.skateshop.ui.features.selection.SelectionScreen
import br.com.skateshop.ui.features.home.HomeScreen
import br.com.skateshop.ui.features.details.DetalheProdutoScreen
import br.com.skateshop.ui.features.cart.CartScreen
// IMPORTAR NOVOS
import br.com.skateshop.ui.features.cart.CartViewModel
import br.com.skateshop.ui.features.cart.CartViewModelFactory
import br.com.skateshop.ui.features.checkout.CheckoutScreen
import br.com.skateshop.ui.features.checkout.OrderSuccessScreen
import br.com.skateshop.ui.features.admin.AdminLoginScreen
import br.com.skateshop.ui.features.admin.AdminHomeScreen
import br.com.skateshop.ui.features.admin.AdminEditProductScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

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

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(context))
    val adminViewModel: AdminViewModel = viewModel(factory = AdminViewModelFactory(context))
    // ADICIONAR ESTA LINHA
    val cartViewModel: CartViewModel = viewModel(factory = CartViewModelFactory(context))

    NavHost(navController = navController, startDestination = Routes.HOME_SCREEN) {

        composable(Routes.TELA_SELECAO_MODO) {
            SelectionScreen(
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
                    navController.navigate(Routes.CARRINHO_SCREEN)
                },
                onNavigateBack = {
                    navController.navigate(Routes.TELA_SELECAO_MODO) {
                        popUpTo(Routes.HOME_SCREEN) { inclusive = true }
                    }
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
                // ATUALIZAR onAddToCartClick
                onAddToCartClick = { produto ->
                    cartViewModel.adicionarItem(produto) // Adiciona ao VM
                    navController.navigate(Routes.CARRINHO_SCREEN)
                }
            )
        }

        composable(Routes.CARRINHO_SCREEN) {
            CartScreen(
                viewModel = cartViewModel, // Passar o VM
                onBack = { navController.popBackStack() },
                onCheckoutClick = { navController.navigate(Routes.CHECKOUT_SCREEN) }
            )
        }
        composable(Routes.CHECKOUT_SCREEN) {
            CheckoutScreen(
                onBack = { navController.popBackStack() },
                onOrderSuccess = {
                    cartViewModel.limparCarrinho() // Limpa o carrinho
                    navController.navigate(Routes.PEDIDO_SUCESSO_SCREEN) {
                        // Limpa a pilha de navegação até a Home
                        popUpTo(Routes.HOME_SCREEN) { inclusive = false }
                    }
                }
            )
        }
        composable(Routes.PEDIDO_SUCESSO_SCREEN) {
            OrderSuccessScreen(
                onContinueShopping = {
                    navController.navigate(Routes.HOME_SCREEN) {
                        popUpTo(Routes.HOME_SCREEN) { inclusive = true }
                    }
                }
            )
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
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}