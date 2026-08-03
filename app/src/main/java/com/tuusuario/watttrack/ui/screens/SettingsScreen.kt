package com.tuusuario.watttrack.ui.screens
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.tuusuario.watttrack.viewmodel.WattTrackViewModel

@Composable
fun SettingsScreen(viewModel: WattTrackViewModel, onVolver: () -> Unit) {
    Text("Ajustes y Backup CSV")
}
