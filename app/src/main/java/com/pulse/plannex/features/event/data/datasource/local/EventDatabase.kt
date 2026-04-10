package com.pulse.plannex.features.event.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pulse.plannex.features.event.data.datasource.local.dao.EventDao
import com.pulse.plannex.features.event.data.datasource.local.entity.EventEntity

@Database(entities = [EventEntity::class], version = 1, exportSchema = false)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
}
