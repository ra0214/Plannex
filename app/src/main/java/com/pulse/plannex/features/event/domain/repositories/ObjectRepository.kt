package com.pulse.plannex.features.event.domain.repositories

import com.pulse.plannex.features.event.domain.entities.Evento

interface ObjectRepository {
    suspend fun getEventos(): List<Evento>
    suspend fun createEvento(nombre: String, fecha: String): Evento
    suspend fun getEvento(id: Int): Evento
    suspend fun updateEvento(id: Int, nombre: String, fecha: String): Evento
    suspend fun deleteEvento(id: Int)
}