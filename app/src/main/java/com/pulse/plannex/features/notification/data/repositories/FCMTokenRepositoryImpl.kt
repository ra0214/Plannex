package com.pulse.plannex.features.notification.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.pulse.plannex.core.network.EventApi
import com.pulse.plannex.core.network.FcmTokenRequest
import com.pulse.plannex.features.notification.domain.repositories.FCMTokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FCMTokenRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val api: EventApi
) : FCMTokenRepository {

    private object PreferencesKeys {
        val FCM_TOKEN = stringPreferencesKey("fcm_token")
    }

    override suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.FCM_TOKEN] = token
        }
    }

    override fun getToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.FCM_TOKEN]
        }
    }

    override suspend fun registerTokenOnServer(userId: Int, token: String): Result<Unit> = runCatching {
        val response = api.registerFcmToken(userId, FcmTokenRequest(token))
        if (!response.isSuccessful) throw Exception("Failed to register token")
    }

    override suspend fun deleteTokenFromServer(userId: Int): Result<Unit> = runCatching {
        val response = api.deleteFcmToken(userId)
        if (!response.isSuccessful) throw Exception("Failed to delete token")
    }
}
