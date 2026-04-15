package com.pulse.plannex.features.auth.domain.repositories

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun saveToken(token: String)
    fun getToken(): Flow<String?>
    suspend fun saveUserId(userId: Int)
    fun getUserId(): Flow<Int?>
    suspend fun clearToken()
}
