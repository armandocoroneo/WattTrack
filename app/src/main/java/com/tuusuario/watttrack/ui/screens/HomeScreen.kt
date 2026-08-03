package com.tuusuario.watttrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tuusuario.watttrack.data.Meter
import com.tuusuario.watttrack.viewmodel.WattTrackViewModel

@Composable
fun HomeScreen(viewModel: WattTrackViewModel) {
    val meters by viewModel.meters.collectAsState()
    val selected by viewModel.selectedMeter.collectAsState()

    LaunchedEffect(Unit) {
        if (meters.isEmpty()) {
            viewModel.addSampleData()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0F))
            .padding(16.dp)
    ) {
        Text(
            text = "⚡ WattTrack",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = "Tus medidores",
            fontSize = 12.sp,
            color = Color(0xFF666666),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (meters.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Cargando...", color = Color(0xFF666666))
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(meters) { meter ->
                    MeterCard(meter = meter, isSelected = meter.id == selected?.id) {
                        viewModel.selectMeter(meter)
                    }
                }
            }
        }
    }
}

@Composable
fun MeterCard(meter: Meter, isSelected: Boolean, onClick: () -> Unit) {
    val borderColor = if (isSelected) Color(0xFFF59E0B) else Color(0xFF222222)
    val bgColor = Color(0xFF111118)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = meter.icon, fontSize = 28.sp, modifier = Modifier.padding(end = 12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = meter.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = if (meter.type == "MAIN") "Medidor Principal" else "Medidor Secundario",
                    fontSize = 11.sp,
                    color = Color(0xFF666666)
                )
            }
            if (isSelected) {
                Text("●", color = Color(0xFFF59E0B), fontSize = 12.sp)
            }
        }
    }
}
