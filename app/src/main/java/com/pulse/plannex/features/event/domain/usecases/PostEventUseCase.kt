package com.pulse.plannex.features.event.domain.usecases

class PostEventUseCase (
    private val repository: ObjectRepository
) {
    suspend fun getObjects(): Result<List<Object>> {
        return try {
            Result.success(repository.getObjects())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createObject(nombre: String, fecha: String): Result<Object> {
        return try {
            if (nombre.isBlank() || fecha.isBlank()) {
                return Result.failure(Exception("El nombre y la fecha no pueden estar vacíos"))
            }
            Result.success(repository.createObject(nombre, fecha))
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}