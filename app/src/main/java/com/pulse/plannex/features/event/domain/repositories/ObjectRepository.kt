package com.pulse.plannex.features.event.domain.repositories

import com.pulse.plannex.features.event.domain.entities.Evento

interface ObjectRepository {
    suspend fun getEventos(): List<Evento>
    suspend fun createEvento(nombre: String, fecha: String): Evento
    suspend fun getEvento(id: Int): Evento
    // Cambiado para que no devuelva nada, ya que la API no devuelve el objeto actualizado
    suspend fun updateEvento(id: Int, nombre: String, fecha: String)
    suspend fun deleteEvento(id: Int)
}