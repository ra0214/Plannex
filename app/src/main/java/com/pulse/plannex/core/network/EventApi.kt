package com.pulse.plannex.core.network

import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

data class EventoDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("nombre") val nombre: String? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("fecha") val fecha: String? = null,
    @SerializedName("date") val date: String? = null,
    @SerializedName("latitude") val latitude: Double? = null,
    @SerializedName("longitude") val longitude: Double? = null,
    @SerializedName("latitud") val latitud: Double? = null,
    @SerializedName("longitud") val longitud: Double? = null,
    @SerializedName("qr_code_data") val qrCodeData: String? = null,
    @SerializedName("created_by") val createdBy: Int? = null
)

data class EventosResponse(
    @SerializedName("eventos") val eventos: List<EventoDto>? = null
)

data class LoginRequest(val userName: String, val password: String)
data class LoginResponse(val status: String, val message: String?, val token: String? = null, val userId: Int? = null)
data class RegisterRequest(val userName: String, val email: String, val password: String)

data class FcmTokenRequest(
    @SerializedName("token") val token: String
)

data class InviteRequest(
    @SerializedName("user_id") val userId: Int
)

interface EventApi {
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("user")
    suspend fun register(@Body request: RegisterRequest): Response<ResponseBody>

    @GET("eventos")
    suspend fun getEventos(@Query("created_by") createdBy: Int? = null): Response<EventosResponse>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("eventos")
    suspend fun createEvento(@Body evento: EventoDto): Response<ResponseBody>

    @GET("eventos/{id}")
    suspend fun getEvento(@Path("id") id: Int): EventoDto

    @PUT("eventos/{id}")
    suspend fun updateEvento(@Path("id") id: Int, @Body evento: EventoDto): Response<ResponseBody>

    @DELETE("eventos/{id}")
    suspend fun deleteEvento(@Path("id") id: Int): Response<ResponseBody>

    @POST("users/{userId}/fcm-token")
    suspend fun registerFcmToken(@Path("userId") userId: Int, @Body request: FcmTokenRequest): Response<ResponseBody>

    @DELETE("users/{userId}/fcm-token")
    suspend fun deleteFcmToken(@Path("userId") userId: Int): Response<ResponseBody>

    @POST("eventos/{eventoId}/invitar")
    suspend fun inviteUser(@Path("eventoId") eventoId: Int, @Body request: InviteRequest): Response<ResponseBody>
}
