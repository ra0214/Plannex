package com.pulse.plannex.core.network

import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

data class EventoDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("date") val date: String? = null,
    @SerializedName("latitude") val latitude: Double? = null,
    @SerializedName("longitude") val longitude: Double? = null,
    @SerializedName("qr_code_data") val qrCodeData: String? = null,
    @SerializedName("created_by") val createdBy: Int? = null
)

data class EventosResponse(
    @SerializedName("eventos") val eventos: List<EventoDto>? = null
)

interface EventApi {
    @Headers("Accept: application/json")
    @GET("eventos")
    suspend fun getEventos(
        @Query("created_by") createdBy: Int? = null
    ): Response<EventosResponse>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("eventos")
    suspend fun createEvento(@Body evento: EventoDto): Response<ResponseBody>

    @GET("eventos/{id}")
    suspend fun getEvento(@Path("id") id: Int): EventoDto

    @PUT("eventos/{id}")
    suspend fun updateEvento(@Path("id") id: Int, @Body evento: EventoDto): Response<ResponseBody>

    @DELETE("eventos/{id}")
    suspend fun deleteEvento(@Path("id") id: Int): Response<ResponseBody>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("user")
    suspend fun register(@Body request: RegisterRequest): Response<ResponseBody>
}

data class LoginRequest(val userName: String, val password: String)
data class LoginResponse(val status: String, val message: String?, val token: String? = null)

data class RegisterRequest(
    val userName: String,
    val email: String,
    val password: String
)
