package com.tuusuario.watttrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tuusuario.watttrack.viewmodel.WattTrackViewModel

@Composable
fun QuickReadScreen(viewModel: WattTrackViewModel) {
    val meters by viewModel.meters.collectAsState()
    val readings = remember { mutableStateMapOf<String, String>() }
    val notes = remember { mutableStateMapOf<String, String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0F))
            .padding(16.dp)
    ) {
        Text(
            text = "⌨️ Lectura Rápida",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = "Ingresa las lecturas de todos los medidores",
            fontSize = 12.sp,
            color = Color(0xFF666666),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (meters.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("No hay medidores", color = Color(0xFF666666))
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(meters) { index, meter ->
                    val readingValue = readings[meter.id] ?: ""
                    val noteValue = notes[meter.id] ?: ""

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF111118), RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "${meter.icon} ${meter.name}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Medidor #${index + 1}",
                            fontSize = 11.sp,
                            color = Color(0xFF666666),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = readingValue,
                            onValueChange = { readings[meter.id] = it },
                            label = { Text("Lectura kWh", color = Color(0xFF666666)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFF59E0B),
                                unfocusedBorderColor = Color(0xFF333333),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = noteValue,
                            onValueChange = { notes[meter.id] = it },
                            label = { Text("Nota opcional", color = Color(0xFF666666)) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFF59E0B),
                                unfocusedBorderColor = Color(0xFF333333),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                    }
                }
            }

            Button(
                onClick = {
                    meters.forEach { meter ->
                        val value = readings[meter.id]?.toIntOrNull()
                        if (value != null) {
                            viewModel.addReading(
                                valueKwh = value,
                                meterId = meter.id,
                                note = notes[meter.id]?.ifBlank { null },
                                tariff = "Punta"
                            )
                        }
                    }
                    readings.clear()
                    notes.clear()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E))
            ) {
                Text("💾 Guardar Todas las Lecturas", fontWeight = FontWeight.Bold)
            }
        }
    }
}
