// MainActivity.kt
package com.watttrack.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.watttrack.app.ui.screens.BalanceScreen
import com.watttrack.app.ui.screens.HistoryScreen
import com.watttrack.app.ui.screens.HomeScreen
import com.watttrack.app.ui.screens.QuickReadScreen
import com.watttrack.app.ui.screens.SettingsScreen
import com.watttrack.app.viewmodel.WattTrackViewModel

sealed class Screen(val route: String, val title: String, val icon: String) {
    object Home : Screen("home", "Inicio", "🏠")
    object QuickRead : Screen("quickRead", "Lectura", "⚡")
    object Settings : Screen("settings", "Ajustes", "⚙️")
    object History : Screen("history", "Historial", "📊")
    object Balance : Screen("balance", "Balance", "⚖️")
}

class MainActivity : ComponentActivity() {

    private val viewModel: WattTrackViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val fondoOscuro = Color(0xFF0A0A0F)
            val acentoAmbar = Color(0xFFF59E0B)
            val superficieOscura = Color(0xFF111118)

            val wattTrackColorScheme = darkColorScheme(
                primary = acentoAmbar,
                background = fondoOscuro,
                surface = superficieOscura,
                onPrimary = fondoOscuro,
                onBackground = Color.White,
                onSurface = Color.White
            )

            MaterialTheme(
                colorScheme = wattTrackColorScheme
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainAppScaffold(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun MainAppScaffold(viewModel: WattTrackViewModel) {
    val navController = rememberNavController()
    val items = listOf(Screen.Home, Screen.QuickRead, Screen.Settings)

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            
            val mostrarBottomBar = currentRoute in items.map { it.route }

            if (mostrarBottomBar) {
                NavigationBar(
                    containerColor = Color(0xFF0A0A0F),
                    tonalElevation = 0.dp
                ) {
                    items.forEach { screen ->
                        val esSeleccionado = currentRoute == screen.route
                        NavigationBarItem(
                            selected = esSeleccionado,
                            onClick = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            label = { 
                                Text(
                                    text = screen.title, 
                                    color = if (esSeleccionado) Color(0xFFF59E0B) else Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = if (esSeleccionado) FontWeight.Bold else FontWeight.Normal
                                ) 
                            },
                            icon = { 
                                Text(text = screen.icon, fontSize = 20.sp)
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color(0xFFF59E0B).copy(alpha = 0.15f)
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onAgregarMedidorClick = { navController.navigate(Screen.Settings.route) },
                    onMedidorClick = { meterId ->
                        viewModel.seleccionarMedidor(meterId)
                        navController.navigate(Screen.History.route)
                    },
                    onNavegarLecturaRapida = { navController.navigate(Screen.QuickRead.route) },
                    onNavegarAjustes = { navController.navigate(Screen.Settings.route) }
                )
            }
            composable(Screen.QuickRead.route) {
                QuickReadScreen(
                    viewModel = viewModel,
                    onGuardarExitoso = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen(
                    viewModel = viewModel,
                    onVolver = { navController.navigateUp() }
                )
            }
            composable(Screen.History.route) {
                HistoryScreen(
                    viewModel = viewModel,
                    onVolver = { navController.navigateUp() }
                )
            }
            composable(Screen.Balance.route) {
                BalanceScreen(
                    viewModel = viewModel,
                    onNavegarAjustes = { navController.navigate(Screen.Settings.route) },
                    onVolver = { navController.navigateUp() }
                )
            }
        }
    }
}
