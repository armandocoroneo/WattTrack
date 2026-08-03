package com.tuusuario.watttrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tuusuario.watttrack.viewmodel.WattTrackViewModel

@Composable
fun SettingsScreen(viewModel: WattTrackViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0F))
            .padding(16.dp)
    ) {
        Text(
            text = "⚙️ Ajustes",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = "Configuración y herramientas",
            fontSize = 12.sp,
            color = Color(0xFF666666),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        SettingButton("📤 Exportar CSV a Drive", "Genera archivo de respaldo") {}
        SettingButton("📥 Restaurar desde archivo", "Carga datos desde CSV") {}
        SettingButton("🗑️ Borrar todos los datos", "Elimina todo el historial", Color(0xFFEF4444)) {}
        
        Spacer(modifier = Modifier.weight(1f))
        
        Text(
            text = "WattTrack v1.0\nApp 100% offline • Sin login",
            fontSize = 11.sp,
            color = Color(0xFF444444),
            lineHeight = 16.sp
        )
    }
}

@Composable
fun SettingButton(title: String, subtitle: String, color: Color = Color(0xFFF59E0B), onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF111118)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Text(title, color = color, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(subtitle, color = Color(0xFF666666), fontSize = 11.sp)
        }
    }
}
