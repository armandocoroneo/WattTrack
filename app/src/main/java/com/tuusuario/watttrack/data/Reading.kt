package com.tuusuario.watttrack.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "readings",
    foreignKeys = [
        ForeignKey(
            entity = Meter::class,
            parentColumns = ["id"],
            childColumns = ["meterId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["meterId"])]
)
data class Reading(
    @PrimaryKey
    val id: String,
    val meterId: String,
    val timestamp: Long,
    val valueKwh: Int,
    val previousKwh: Int,
    val consumption: Int,
    val tariff: String,
    val pricePerKwh: Float,
    val costTotal: Float,
    val note: String?,
    val inputMethod: String,
    val photoPath: String?
)
