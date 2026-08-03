package com.tuusuario.watttrack.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tuusuario.watttrack.data.AppDatabase
import com.tuusuario.watttrack.data.Meter
import com.tuusuario.watttrack.data.Reading
import com.tuusuario.watttrack.data.SampleData
import com.tuusuario.watttrack.data.repository.WattTrackRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class WattTrackViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: WattTrackRepository
    private val _meters = MutableStateFlow<List<Meter>>(emptyList())
    val meters: StateFlow<List<Meter>> = _meters.asStateFlow()
    private val _readings = MutableStateFlow<List<Reading>>(emptyList())
    val readings: StateFlow<List<Reading>> = _readings.asStateFlow()
    var currentSelectedMeterId: String? = null

    init {
        val database = AppDatabase.getDatabase(application)
        repository = WattTrackRepository(database.meterDao())
        viewModelScope.launch {
            SampleData.populateDatabase(database.meterDao())
            cargarMedidores()
        }
    }

    fun cargarMedidores() {
        viewModelScope.launch { _meters.value = repository.getMeters() }
    }

    fun seleccionarMedidor(meterId: String) {
        currentSelectedMeterId = meterId
        cargarLecturas(meterId)
    }

    fun cargarLecturas(meterId: String) {
        viewModelScope.launch { _readings.value = repository.getReadings(meterId) }
    }

    fun addMeter(name: String, type: String, parentId: String?, colorHex: String, icon: String) {
        viewModelScope.launch {
            repository.addMeter(Meter(UUID.randomUUID().toString(), name, type, parentId, colorHex, icon))
            cargarMedidores()
        }
    }

    fun addReading(valueKwh: Int, meterId: String, note: String?, tariff: String) {
        viewModelScope.launch {
            val ultima = repository.getLastReading(meterId)
            val prev = ultima?.valueKwh ?: valueKwh
            val cons = valueKwh - prev
            val precio = when (tariff) { "Valle" -> 0.12f; "Punta" -> 0.18f; "Pico" -> 0.24f; else -> 0.18f }
            val total = cons * precio
            repository.addReading(Reading(UUID.randomUUID().toString(), meterId, System.currentTimeMillis(), valueKwh, prev, cons, tariff, precio, total, note?.take(120), "MANUAL", null))
            cargarLecturas(meterId)
        }
    }

    fun deleteReadingAndRecalculate(reading: Reading) {
        viewModelScope.launch {
            repository.deleteReading(reading)
            val rest = repository.getReadings(reading.meterId).sortedBy { it.timestamp }
            recalcularSecuencia(reading.meterId, rest)
            cargarLecturas(reading.meterId)
        }
    }

    fun updateReadingAndRecalculate(reading: Reading, newValue: Int, newNote: String?) {
        viewModelScope.launch {
            repository.updateReading(reading.copy(valueKwh = newValue, note = newNote?.take(120)))
            val mod = repository.getReadings(reading.meterId).sortedBy { it.timestamp }
            recalcularSecuencia(reading.meterId, mod)
            cargarLecturas(reading.meterId)
        }
    }

    private suspend fun recalcularSecuencia(meterId: String, lista: List<Reading>) {
        var base = 0
        lista.forEachIndexed { i, r ->
            val prev = if (i == 0) r.valueKwh else base
            val cons = r.valueKwh - prev
            repository.updateReading(r.copy(previousKwh = prev, consumption = cons, costTotal = cons * r.pricePerKwh))
            base = r.valueKwh
        }
    }
}
