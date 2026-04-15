package com.pulse.plannex.core.network

import android.util.Log
import com.pulse.plannex.features.auth.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val authRepository: AuthRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            authRepository.getToken().firstOrNull()
        }

        val requestBuilder = chain.request().newBuilder()
        
        if (!token.isNullOrEmpty()) {
            // Log para verificar que el token se está enviando
            Log.d("AuthInterceptor", "Inyectando Token: Bearer $token")
            requestBuilder.header("Authorization", "Bearer $token")
        } else {
            Log.w("AuthInterceptor", "No se encontró token para esta petición: ${chain.request().url}")
        }

        return chain.proceed(requestBuilder.build())
    }
}
