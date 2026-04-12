package com.pulse.plannex.features.notification.domain.repositories

interface InvitationRepository {
    suspend fun inviteUser(eventoId: Int, userId: Int): Result<Unit>
}
