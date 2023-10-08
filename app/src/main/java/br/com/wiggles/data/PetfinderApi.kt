package br.com.wiggles.data

import br.com.wiggles.BuildConfig
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import javax.inject.Inject

interface PetfinderAuthApi {
    @FormUrlEncoded
    @POST("oauth2/token")
    suspend fun getToken(
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String
    ): Response<TokenResponse>
}

interface PetfinderApi {

    @GET("animals")
    suspend fun getPets(): Response<PetsResponse>

    @GET("animals/{id}")
    suspend fun getPet(@Path("id") petId: String): Response<SinglePetResponse>
}


class PetfinderAuthNetwork @Inject constructor(
    private val networkApi: PetfinderAuthApi
) {
    suspend fun getToken(): TokenResponse? = networkApi
        .getToken(
            "client_credentials",
            BuildConfig.PETFINDER_CLIENT_ID,
            BuildConfig.PETFINDER_CLIENT_SECRET
        )
        .body()
}

class PetfinderNetwork @Inject constructor(
    private val networkApi: PetfinderApi
) {

    suspend fun getPets(): PetsResponse? = networkApi
        .getPets()
        .body()

    suspend fun getPet(petId: String): PetResponse? = networkApi
        .getPet(petId)
        .body()
        ?.animal
}