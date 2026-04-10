package com.pulse.plannex.features.event.data.repositories

import com.pulse.plannex.core.network.EventApi
import com.pulse.plannex.core.network.EventoDto
import com.pulse.plannex.features.event.data.datasource.local.dao.EventDao
import com.pulse.plannex.features.event.data.datasource.local.mapper.toDomain
import com.pulse.plannex.features.event.data.datasource.local.mapper.toEntity
import com.pulse.plannex.features.event.data.datasource.remote.mapper.toDomain
import com.pulse.plannex.features.event.domain.entities.Evento
import com.pulse.plannex.features.event.domain.repositories.ObjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ObjectsRepositoryImlp @Inject constructor(
    private val api: EventApi,
    private val eventDao: EventDao
) : ObjectRepository {

    override fun getEventosFlow(): Flow<List<Evento>> {
        return eventDao.getAllEvents().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getUpcomingEventosFlow(): Flow<List<Evento>> {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDate = sdf.format(Date())
        return eventDao.getUpcomingEvents(currentDate).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refreshEventos(): Result<List<Evento>> {
        return try {
            val response = api.getEventos()
            if (response.isSuccessful) {
                // Ajuste crítico: Accedemos a .eventos del objeto de respuesta
                val remoteEvents = response.body()?.eventos ?: emptyList()
                val allEventsDomain = remoteEvents.map { it.toDomain() }
                
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val currentDate = sdf.format(Date())
                
                val top3Upcoming = allEventsDomain
                    .filter { it.fecha >= currentDate }
                    .sortedBy { it.fecha }
                    .take(3)
                    .map { it.toEntity() }

                eventDao.clearAllEvents()
                eventDao.insertEvents(top3Upcoming)
                
                Result.success(allEventsDomain)
            } else {
                Result.failure(Exception("Error servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createEvento(nombre: String, fecha: String, latitud: Double?, longitud: Double?): Result<Evento> {
        return try {
            val dto = EventoDto(
                title = nombre, 
                date = fecha, 
                latitude = latitud, 
                longitude = longitud, 
                qrCodeData = "QR-${System.currentTimeMillis()}", 
                createdBy = 1
            )
            val response = api.createEvento(dto)
            if (response.isSuccessful) {
                refreshEventos()
                Result.success(Evento(id = -1, nombre = nombre, fecha = fecha, latitud = latitud, longitud = longitud))
            } else {
                Result.failure(Exception("Error al crear"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getEvento(id: Int): Evento {
        return api.getEvento(id).toDomain()
    }

    override suspend fun updateEvento(id: Int, nombre: String, fecha: String, latitud: Double?, longitud: Double?): Result<Unit> {
        return try {
            val dto = EventoDto(id = id, title = nombre, date = fecha, latitude = latitud, longitude = longitud)
            val response = api.updateEvento(id, dto)
            if (response.isSuccessful) {
                refreshEventos()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al actualizar"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteEvento(id: Int): Result<Unit> {
        return try {
            val response = api.deleteEvento(id)
            if (response.isSuccessful) {
                eventDao.deleteEventById(id)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
