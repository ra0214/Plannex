package com.pulse.plannex.features.event.data.datasource.local.dao

import androidx.room.*
import com.pulse.plannex.features.event.data.datasource.local.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM events ORDER BY date ASC")
    fun getAllEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE date >= :currentDate ORDER BY date ASC LIMIT 3")
    fun getUpcomingEvents(currentDate: String): Flow<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Query("DELETE FROM events")
    suspend fun clearAllEvents()

    @Query("DELETE FROM events WHERE id = :eventId")
    suspend fun deleteEventById(eventId: Int)
}
