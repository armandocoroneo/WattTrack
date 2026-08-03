package com.tuusuario.watttrack.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meters")
data class Meter(
    @PrimaryKey
    val id: String,
    val name: String,
    val type: String,
    val parentId: String?,
    val colorHex: String,
    val icon: String,
    val thresholdPercent: Float = 10f,
    val walkMarginKwh: Float = 20f
)
