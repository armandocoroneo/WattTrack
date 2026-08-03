package com.tuusuario.watttrack.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tuusuario.watttrack.ui.screens.*
import com.tuusuario.watttrack.viewmodel.WattTrackViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object QuickRead : Screen("quickRead")
    object History : Screen("history")
    object Balance : Screen("balance")
    object Settings : Screen("settings")
}

@Composable
fun NavGraph(navController: NavHostController, viewModel: WattTrackViewModel) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen(viewModel, { navController.navigate(Screen.Settings.route) }, { id -> viewModel.seleccionarMedidor(id); navController.navigate(Screen.History.route) }, { navController.navigate(Screen.QuickRead.route) }, { navController.navigate(Screen.Settings.route) }) }
        composable(Screen.QuickRead.route) { QuickReadScreen(viewModel, { navController.navigate(Screen.Home.route) }) }
        composable(Screen.History.route) { HistoryScreen(viewModel, { navController.navigateUp() }) }
        composable(Screen.Balance.route) { BalanceScreen(viewModel, { navController.navigate(Screen.Settings.route) }, { navController.navigateUp() }) }
        composable(Screen.Settings.route) { SettingsScreen(viewModel, { navController.navigateUp() }) }
    }
}
