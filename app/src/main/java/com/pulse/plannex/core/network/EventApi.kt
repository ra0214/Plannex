package com.pulse.plannex.core.network

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// --- Modelos de Datos ---

data class EventoDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("date") val date: String,
    @SerializedName("latitude") val latitude: Double? = null,
    @SerializedName("longitude") val longitude: Double? = null,
    @SerializedName("qr_code_data") val qrCodeData: String? = null,
    @SerializedName("created_by") val createdBy: Int? = null
)

data class EventosResponse(
    @SerializedName("eventos") val eventos: List<EventoDto>?
)

data class MessageResponse(
    @SerializedName("message") val message: String
)

data class LoginRequest(
    @SerializedName("userName") val userName: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String?,
    @SerializedName("token") val token: String? = null
)

data class RegisterRequest(
    @SerializedName("userName") val userName: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class InviteRequest(
    @SerializedName("user_id") val userId: Int
)

data class AsistenciaRequest(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("estado") val estado: String
)

interface EventApi {
    @GET("eventos")
    suspend fun getEventos(): EventosResponse

    @POST("eventos")
    suspend fun createEvento(@Body evento: EventoDto): MessageResponse

    @GET("eventos/{id}")
    suspend fun getEvento(@Path("id") id: Int): EventoDto

    @PUT("eventos/{id}")
    suspend fun updateEvento(@Path("id") id: Int, @Body evento: EventoDto): EventoDto

    @DELETE("eventos/{id}")
    suspend fun deleteEvento(@Path("id") id: Int)

    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("user")
    suspend fun register(@Body request: RegisterRequest): MessageResponse

    @POST("eventos/{id}/invitar")
    suspend fun invitar(@Path("id") id: Int, @Body inviteRequest: InviteRequest)

    @PUT("eventos/{id}/asistencia")
    suspend fun updateAsistencia(@Path("id") id: Int, @Body asistenciaRequest: AsistenciaRequest)
}
