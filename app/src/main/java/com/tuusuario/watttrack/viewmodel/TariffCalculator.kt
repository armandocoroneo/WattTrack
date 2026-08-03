package com.tuusuario.watttrack.viewmodel

object TariffCalculator {
    fun getPriceForHour(hour: Int): Float = when (hour) {
        in 0..5 -> 0.12f
        in 6..17 -> 0.18f
        in 18..23 -> 0.24f
        else -> 0.18f
    }
    fun getTariffName(hour: Int): String = when (hour) {
        in 0..5 -> "Valle"
        in 6..17 -> "Punta"
        in 18..23 -> "Pico"
        else -> "Punta"
    }
}
