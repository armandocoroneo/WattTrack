package com.tuusuario.watttrack.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface MeterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeter(meter: Meter)

    @Update
    suspend fun updateMeter(meter: Meter)

    @Delete
    suspend fun deleteMeter(meter: Meter)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReading(reading: Reading)

    @Update
    suspend fun updateReading(reading: Reading)

    @Delete
    suspend fun deleteReading(reading: Reading)

    @Query("SELECT * FROM meters")
    suspend fun getAllMeters(): List<Meter>

    @Query("SELECT * FROM readings WHERE meterId = :meterId ORDER BY timestamp DESC")
    suspend fun getReadingsForMeter(meterId: String): List<Reading>

    @Query("SELECT * FROM readings WHERE meterId = :meterId AND timestamp BETWEEN :start AND :end ORDER BY timestamp DESC")
    suspend fun getReadingsBetween(meterId: String, start: Long, end: Long): List<Reading>

    @Query("SELECT * FROM readings WHERE meterId = :meterId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastReading(meterId: String): Reading?
}
