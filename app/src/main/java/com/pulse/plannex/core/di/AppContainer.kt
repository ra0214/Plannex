package com.pulse.plannex.core.di

import com.pulse.plannex.core.network.EventApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://localhost:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val eventApi: EventApi by lazy {
        retrofit.create(EventApi::class.java)
    }

//    val eventosRepository: EventosRepository by lazy {
//        EventosRepositoryImpl(eventApi)
//    }

}