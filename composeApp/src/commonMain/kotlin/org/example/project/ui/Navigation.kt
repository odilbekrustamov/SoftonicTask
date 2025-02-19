package org.example.project.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

object AppConstants {
    const val BACK_CLICK_ROUTE = "BACK_CLICK_ROUTE"
}

@Composable
fun QRKitNav() {
    val navController = rememberNavController()

    NavHostMain(
        navController = navController,
        onNavigate = { routeName ->
            navigateTo(routeName, navController)
        }
    )
}

@Composable
fun NavHostMain(
    navController: NavHostController = rememberNavController(),
    onNavigate: (rootName: String) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.QRKitMainScreen.route,
        modifier = Modifier
            .fillMaxSize(),
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500)
            )
        }
    ) {
        composable(route = AppScreen.QRKitMainScreen.route) {
            QRKitMainScreen(onNavigate)
        }
        composable(route = AppScreen.QRGenerator.route) {
            QrGeneratorView(onNavigate)
        }

        composable(route = AppScreen.QRScanner.route) {
            QrScannerView(onNavigate = onNavigate)
        }

    }
}

fun navigateTo(
    routeName: String,
    navController: NavController
) {
    when (routeName) {
        AppConstants.BACK_CLICK_ROUTE -> {
            navController.popBackStack()
        }

        else -> {
            navController.navigate(routeName)
        }
    }
}

sealed class AppScreen(val route: String) {
    data object QRKitMainScreen : AppScreen("nav_qr_main_screen")
    data object QRGenerator : AppScreen("nav_qr_generator")
    data object QRScanner : AppScreen("nav_qr_scanner")
}

