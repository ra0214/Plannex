package com.pulse.plannex.features.event.data.repositories

class ObjectsRepositoryImlp(
    private val api: ApiPlace
) : ObjectsRepository  {
    override suspend fun getObjects(): List<Object> {
        return api.getObjects().map { it.toDomain() }
    }

    override suspend fun postObject(nombre: String, fecha: String) {
        val dto=PostDto(nombre,fecha)
        return api.postObject(dto).toDomain()
    }
}