package com.pulse.plannex.features.event.domain.repositories

interface ObjectRepository {
    suspend fun getObjects(): List<Object>
    suspend fun createObject(nombre: String, fecha: String): Object
}