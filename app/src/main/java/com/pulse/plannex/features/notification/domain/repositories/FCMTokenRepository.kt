package com.pulse.plannex.features.notification.domain.repositories

import kotlinx.coroutines.flow.Flow

interface FCMTokenRepository {
    suspend fun saveToken(token: String)
    fun getToken(): Flow<String?>
    suspend fun registerTokenOnServer(userId: Int, token: String): Result<Unit>
    suspend fun deleteTokenFromServer(userId: Int): Result<Unit>
}
