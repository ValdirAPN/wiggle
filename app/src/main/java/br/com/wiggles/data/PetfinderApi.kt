package br.com.wiggles.data

import br.com.wiggles.BuildConfig
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PetfinderApi {
    @FormUrlEncoded
    @POST("oauth2/token")
    suspend fun getToken(
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String
    ): Response<TokenResponse>
}

private const val PetfinderBaseUrl = "https://api.petfinder.com/v2/"

class PetfinderNetwork {

    private val networkApi = Retrofit.Builder()
        .baseUrl(PetfinderBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PetfinderApi::class.java)

    suspend fun getToken(): TokenResponse? = networkApi
        .getToken(
            "client_credentials",
            BuildConfig.PETFINDER_CLIENT_ID,
            BuildConfig.PETFINDER_CLIENT_SECRET
        )
        .body()
}