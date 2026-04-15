package com.pulse.plannex.features.notification.data.repositories

import com.pulse.plannex.core.network.EventApi
import com.pulse.plannex.core.network.InviteRequest
import com.pulse.plannex.core.network.UserDto
import com.pulse.plannex.features.notification.domain.repositories.InvitationRepository
import javax.inject.Inject

class InvitationRepositoryImpl @Inject constructor(
    private val api: EventApi
) : InvitationRepository {
    override suspend fun inviteUser(eventoId: Int, userId: Int): Result<Unit> = runCatching {
        val response = api.inviteUser(eventoId, InviteRequest(userId))
        if (!response.isSuccessful) throw Exception("Error al invitar usuario")
    }

    override suspend fun getUsers(): Result<List<UserDto>> = runCatching {
        val response = api.getUsers()
        if (response.isSuccessful) {
            response.body()?.users ?: emptyList()
        } else {
            throw Exception("Error al obtener usuarios")
        }
    }
}
