package com.tuusuario.watttrack.data

import androidx.room.*

@Dao
interface MeterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeter(meter: Meter)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReading(reading: Reading)

    @Query("SELECT * FROM meters")
    suspend fun getAllMeters(): List<Meter>

    @Query("SELECT * FROM readings WHERE meterId = :meterId ORDER BY timestamp DESC")
    suspend fun getReadingsForMeter(meterId: String): List<Reading>

    @Query("SELECT * FROM readings WHERE meterId = :meterId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastReading(meterId: String): Reading?

    @Query("SELECT * FROM readings WHERE meterId = :meterId AND timestamp BETWEEN :start AND :end ORDER BY timestamp ASC")
    suspend fun getReadingsBetween(meterId: String, start: Long, end: Long): List<Reading>

    @Update
    suspend fun updateReading(reading: Reading)

    @Delete
    suspend fun deleteReading(reading: Reading)
}
