package com.pulse.plannex.core.network

import com.pulse.plannex.core.Evento
import com.pulse.plannex.core.InviteRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EventApi {

    @POST("eventos")
    suspend fun createEvento(@Body evento: EventoDto): EventoDto


    @GET("eventos")
    suspend fun getEventos(): List<EventoDto>

    @GET("eventos/{id}")
    suspend fun getEvento(@Path("id") id: Int): EventoDto

    @POST("eventos/{id}/invitar")
    suspend fun invitar(@Path("id") id: Int, @Body inviteRequest: InviteRequest)
}