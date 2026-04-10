package com.pulse.plannex.features.event.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val latitude: Double?,
    val longitude: Double?,
    val qrCodeData: String?,
    val createdBy: Int
)
