package com.tuusuario.watttrack.data

class WattTrackRepository(private val meterDao: MeterDao) {
    suspend fun insertMeter(meter: Meter) = meterDao.insertMeter(meter)
    suspend fun insertReading(reading: Reading) = meterDao.insertReading(reading)
    suspend fun getAllMeters() = meterDao.getAllMeters()
    suspend fun getReadingsForMeter(meterId: String) = meterDao.getReadingsForMeter(meterId)
    suspend fun getLastReading(meterId: String) = meterDao.getLastReading(meterId)
    suspend fun deleteReading(reading: Reading) = meterDao.deleteReading(reading)
    suspend fun updateReading(reading: Reading) = meterDao.updateReading(reading)
    suspend fun getReadingsInRange(meterId: String, start: Long, end: Long) = meterDao.getReadingsInRange(meterId, start, end)
}
