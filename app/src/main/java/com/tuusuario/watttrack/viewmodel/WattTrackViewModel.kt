package com.tuusuario.watttrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tuusuario.watttrack.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class WattTrackViewModel(private val repository: WattTrackRepository) : ViewModel() {

    private val _meters = MutableStateFlow<List<Meter>>(emptyList())
    val meters: StateFlow<List<Meter>> = _meters.asStateFlow()

    private val _readings = MutableStateFlow<List<Reading>>(emptyList())
    val readings: StateFlow<List<Reading>> = _readings.asStateFlow()

    private val _selectedMeter = MutableStateFlow<Meter?>(null)
    val selectedMeter: StateFlow<Meter?> = _selectedMeter.asStateFlow()

    init {
        viewModelScope.launch {
            loadMeters()
        }
    }

    suspend fun loadMeters() {
        val list = repository.getAllMeters()
        _meters.value = list
        if (list.isNotEmpty() && _selectedMeter.value == null) {
            _selectedMeter.value = list.first()
            loadReadings(list.first().id)
        }
    }

    suspend fun loadReadings(meterId: String) {
        _readings.value = repository.getReadingsForMeter(meterId)
    }

    fun selectMeter(meter: Meter) {
        _selectedMeter.value = meter
        viewModelScope.launch {
            loadReadings(meter.id)
        }
    }

    fun addReading(valueKwh: Int, meterId: String, note: String?, tariff: String = "Punta") {
        viewModelScope.launch {
            val lastReading = repository.getLastReading(meterId)
            val previousKwh = lastReading?.valueKwh ?: 0
            val consumption = valueKwh - previousKwh
            val price = when (tariff) {
                "Valle" -> 0.12f
                "Pico" -> 0.24f
                else -> 0.18f
            }
            val cost = consumption * price

            val reading = Reading(
                id = UUID.randomUUID().toString(),
                meterId = meterId,
                valueKwh = valueKwh,
                previousKwh = previousKwh,
                consumption = consumption,
                tariff = tariff,
                pricePerKwh = price,
                costTotal = cost,
                note = note,
                inputMethod = "MANUAL"
            )
            repository.insertReading(reading)
            loadReadings(meterId)
        }
    }

    fun deleteReading(reading: Reading) {
        viewModelScope.launch {
            repository.deleteReading(reading)
            loadReadings(reading.meterId)
        }
    }

    fun addSampleData() {
        viewModelScope.launch {
            if (repository.getAllMeters().isEmpty()) {
                val main = Meter("main", "Casa Principal", "MAIN", null, "#F59E0B", "🏠")
                val local = Meter("local", "Local Comercial", "SUB", "main", "#3B82F6", "🏪")
                val depa = Meter("depa", "Depa 3B", "SUB", "main", "#EC4899", "🏢")
                
                repository.insertMeter(main)
                repository.insertMeter(local)
                repository.insertMeter(depa)

                val now = System.currentTimeMillis()
                val day = 86400000L
                
                repository.insertReading(Reading(UUID.randomUUID().toString(), "main", now, 12580, 12450, 130, "Pico", 0.18f, 23.40f, "Lectura inicial", "MANUAL"))
                repository.insertReading(Reading(UUID.randomUUID().toString(), "local", now, 8930, 8810, 120, "Pico", 0.18f, 21.60f, null, "MANUAL"))
                repository.insertReading(Reading(UUID.randomUUID().toString(), "depa", now, 5620, 5550, 70, "Punta", 0.18f, 12.60f, null, "MANUAL"))
                
                loadMeters()
            }
        }
    }
}

class WattTrackViewModelFactory(private val repository: WattTrackRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WattTrackViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WattTrackViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
