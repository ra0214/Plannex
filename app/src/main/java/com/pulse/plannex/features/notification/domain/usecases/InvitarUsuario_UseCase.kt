package com.pulse.plannex.features.notification.domain.usecases

import com.pulse.plannex.features.notification.domain.repositories.InvitationRepository
import javax.inject.Inject

class InvitarUsuario_UseCase @Inject constructor(
    private val repository: InvitationRepository
) {
    suspend operator fun invoke(eventoId: Int, userId: Int): Result<Unit> {
        return repository.inviteUser(eventoId, userId)
    }
}
