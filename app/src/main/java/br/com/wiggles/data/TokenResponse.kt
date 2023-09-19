package br.com.wiggles.data

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("token_type") val type: String,
    @SerializedName("expires_in") val expiresIn: Int,
    @SerializedName("access_token") val token: String
)