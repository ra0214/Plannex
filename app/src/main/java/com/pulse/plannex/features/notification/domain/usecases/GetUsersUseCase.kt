package com.pulse.plannex.features.notification.domain.usecases

import com.pulse.plannex.core.network.UserDto
import com.pulse.plannex.features.notification.domain.repositories.InvitationRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: InvitationRepository
) {
    suspend operator fun invoke(): Result<List<UserDto>> {
        return repository.getUsers()
    }
}
