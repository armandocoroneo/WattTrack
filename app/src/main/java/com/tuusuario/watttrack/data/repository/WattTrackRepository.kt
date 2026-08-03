package com.tuusuario.watttrack.data.repository

import com.tuusuario.watttrack.data.Meter
import com.tuusuario.watttrack.data.MeterDao
import com.tuusuario.watttrack.data.Reading

class WattTrackRepository(private val meterDao: MeterDao) {
    suspend fun addMeter(meter: Meter) = meterDao.insertMeter(meter)
    suspend fun addReading(reading: Reading) = meterDao.insertReading(reading)
    suspend fun getMeters(): List<Meter> = meterDao.getAllMeters()
    suspend fun getReadings(meterId: String): List<Reading> = meterDao.getReadingsForMeter(meterId)
    suspend fun deleteReading(reading: Reading) = meterDao.deleteReading(reading)
    suspend fun updateReading(reading: Reading) = meterDao.updateReading(reading)
    suspend fun getLastReading(meterId: String): Reading? = meterDao.getLastReading(meterId)
    suspend fun getReadingsInRange(meterId: String, start: Long, end: Long): List<Reading> = meterDao.getReadingsBetween(meterId, start, end)
}
