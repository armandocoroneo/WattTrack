package com.tuusuario.watttrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tuusuario.watttrack.data.AppDatabase
import com.tuusuario.watttrack.data.WattTrackRepository
import com.tuusuario.watttrack.ui.screens.HomeScreen
import com.tuusuario.watttrack.ui.screens.QuickReadScreen
import com.tuusuario.watttrack.ui.screens.SettingsScreen
import com.tuusuario.watttrack.viewmodel.WattTrackViewModel
import com.tuusuario.watttrack.viewmodel.WattTrackViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = WattTrackRepository(database.meterDao())
        val viewModelFactory = WattTrackViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, viewModelFactory)[WattTrackViewModel::class.java]

        setContent {
            WattTrackApp(viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WattTrackApp(viewModel: WattTrackViewModel) {
    val navController = rememberNavController()
    
    val items = listOf(
        Screen.Home,
        Screen.QuickRead,
        Screen.Settings
    )
    
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = androidx.compose.ui.graphics.Color(0xFFF59E0B),
            onPrimary = androidx.compose.ui.graphics.Color(0xFF000000),
            background = androidx.compose.ui.graphics.Color(0xFF0A0A0F),
            surface = androidx.compose.ui.graphics.Color(0xFF111118),
            onBackground = androidx.compose.ui.graphics.Color(0xFFE8E8E8),
            onSurface = androidx.compose.ui.graphics.Color(0xFFE8E8E8)
        )
    ) {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = androidx.compose.ui.graphics.Color(0xFF0A0A0F)
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.label) },
                            label = { Text(screen.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = androidx.compose.ui.graphics.Color(0xFFF59E0B),
                                selectedTextColor = androidx.compose.ui.graphics.Color(0xFFF59E0B),
                                unselectedIconColor = androidx.compose.ui.graphics.Color(0xFF666666),
                                unselectedTextColor = androidx.compose.ui.graphics.Color(0xFF666666),
                                indicatorColor = androidx.compose.ui.graphics.Color(0xFF1A1A24)
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Home.route) { HomeScreen(viewModel) }
                composable(Screen.QuickRead.route) { QuickReadScreen(viewModel) }
                composable(Screen.Settings.route) { SettingsScreen(viewModel) }
            }
        }
    }
}

sealed class Screen(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : Screen("home", "Inicio", Icons.Filled.Home)
    object QuickRead : Screen("quickread", "Lectura", Icons.Filled.Edit)
    object Settings : Screen("settings", "Ajustes", Icons.Filled.Settings)
}
