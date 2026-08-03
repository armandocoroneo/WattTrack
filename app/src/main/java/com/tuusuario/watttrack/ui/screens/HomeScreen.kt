package com.tuusuario.watttrack.ui.screens
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.tuusuario.watttrack.viewmodel.WattTrackViewModel

@Composable
fun HomeScreen(viewModel: WattTrackViewModel, onAgregarMedidorClick: () -> Unit, onMedidorClick: (String) -> Unit, onNavegarLecturaRapida: () -> Unit, onNavegarAjustes: () -> Unit) {
    Text("Pantalla de Inicio de WattTrack (Material3)")
}
