package com.pulse.plannex.core.di

import com.pulse.plannex.core.network.EventApi
import com.pulse.plannex.features.event.data.repositories.ObjectsRepositoryImlp
import com.pulse.plannex.features.event.domain.repositories.ObjectRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/") // IP para acceder al localhost desde el emulador de Android
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val eventApi: EventApi by lazy {
        retrofit.create(EventApi::class.java)
    }

    val objectRepository: ObjectRepository by lazy {
        ObjectsRepositoryImlp(eventApi)
    }
}