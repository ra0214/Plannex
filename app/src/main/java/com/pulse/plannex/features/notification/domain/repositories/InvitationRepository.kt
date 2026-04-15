package com.pulse.plannex.features.notification.domain.repositories

import com.pulse.plannex.core.network.UserDto

interface InvitationRepository {
    suspend fun inviteUser(eventoId: Int, userId: Int): Result<Unit>
    suspend fun getUsers(): Result<List<UserDto>>
}
