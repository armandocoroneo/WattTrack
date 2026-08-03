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
            if (repository.getAllMeters().isEmpty()) {
                addSampleData()
            }
            loadMeters()
        }
    }

    private suspend fun loadMeters() {
        val list = repository.getAllMeters()
        _meters.value = list
        if (list.isNotEmpty() && _selectedMeter.value == null) {
            _selectedMeter.value = list.first()
            loadReadings(list.first().id)
        }
    }

    private suspend fun loadReadings(meterId: String) {
        _readings.value = repository.getReadingsForMeter(meterId)
    }

    fun selectMeter(meter: Meter) {
        _selectedMeter.value = meter
        viewModelScope.launch { loadReadings(meter.id) }
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
                inputMethod = "MANUAL",
                photoPath = null
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
            repository.insertMeter(Meter(id = "main", name = "Casa Principal", type = "MAIN", parentId = null, colorHex = "#F59E0B", icon =
