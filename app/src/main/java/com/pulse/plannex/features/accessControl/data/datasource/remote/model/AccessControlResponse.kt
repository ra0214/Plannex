package com.pulse.plannex.features.accessControl.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class AccessControlResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String
)
