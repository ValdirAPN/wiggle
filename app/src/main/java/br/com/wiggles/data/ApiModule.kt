package br.com.wiggles.data

import android.content.Context
import br.com.wiggles.domain.PetfinderTokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun providePetfinderNetwork(
        petfinderApi: PetfinderApi
    ): PetfinderNetwork = PetfinderNetwork(petfinderApi)

    @Provides
    @Singleton
    fun providePetfinderAuthNetwork(
        petfinderAuthApi: PetfinderAuthApi
    ): PetfinderAuthNetwork = PetfinderAuthNetwork(petfinderAuthApi)

    @Provides
    @Singleton
    fun providePetfinderApi(
        petfinderAuthenticator: PetfinderAuthenticator
    ): PetfinderApi {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .authenticator(petfinderAuthenticator)
            .build()

        val petfinderBaseUrl = "https://api.petfinder.com/v2/"

        return Retrofit.Builder()
            .baseUrl(petfinderBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PetfinderApi::class.java)
    }

    @Provides
    @Singleton
    fun providePetfinderAuthApi(): PetfinderAuthApi {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val petfinderBaseUrl = "https://api.petfinder.com/v2/"

        return Retrofit.Builder()
            .baseUrl(petfinderBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PetfinderAuthApi::class.java)
    }

    @Provides
    @Singleton
    fun providePetfinderAuthenticator(
        petfinderAuthApi: PetfinderAuthNetwork,
        petfinderTokenManager: PetfinderTokenManager
    ): PetfinderAuthenticator =
        PetfinderAuthenticator(petfinderAuthApi, petfinderTokenManager)

    @Provides
    @Singleton
    fun providePetfinderTokenManager(
        @ApplicationContext context: Context
    ): PetfinderTokenManager =
        PetfinderTokenManager(context)
}