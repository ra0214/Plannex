package com.pulse.plannex.core.network

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// --- Modelos de Datos para Peticiones y Respuestas ---

data class EventoDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("fecha") val fecha: String
)

data class InviteRequest(
    @SerializedName("user_id") val userId: Int
)

data class AsistenciaRequest(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("estado") val estado: String
)

// Objeto que envuelve la lista de eventos (para GET /eventos)
data class EventosResponse(
    // Making the list nullable to handle cases where the API returns null
    @SerializedName("eventos")
    val eventos: List<EventoDto>?
)

// Objeto para el mensaje de éxito (para POST /eventos)
data class MessageResponse(
    @SerializedName("message")
    val message: String
)

// --- Interfaz de la API ---

interface EventApi {

    @POST("eventos")
    suspend fun createEvento(@Body evento: EventoDto): MessageResponse

    @GET("eventos")
    suspend fun getEventos(): EventosResponse

    @GET("eventos/{id}")
    suspend fun getEvento(@Path("id") id: Int): EventoDto

    @PUT("eventos/{id}")
    suspend fun updateEvento(@Path("id") id: Int, @Body evento: EventoDto): EventoDto

    @DELETE("eventos/{id}")
    suspend fun deleteEvento(@Path("id") id: Int)

    @POST("eventos/{id}/invitar")
    suspend fun invitar(@Path("id") id: Int, @Body inviteRequest: InviteRequest)

    @PUT("eventos/{id}/asistencia")
    suspend fun updateAsistencia(@Path("id") id: Int, @Body asistenciaRequest: AsistenciaRequest)
}
