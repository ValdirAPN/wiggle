package br.com.wiggles.data

import br.com.wiggles.domain.PetfinderTokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class PetfinderAuthenticator @Inject constructor(
    private val petfinderAuthApi: PetfinderAuthNetwork,
    private val petfinderTokenManager: PetfinderTokenManager
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.request.header("Authorization") == null) {
            val token = runBlocking {
                petfinderTokenManager.getToken().first()
            }

            return response.request.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        }

        if (response.code == 401) { // The access token is expired
            return runBlocking {
                try {
                    val newToken = refreshToken() ?: throw Exception("Token not retrieved.")

                    return@runBlocking response.request.newBuilder()
                        .header("Authorization", "Bearer $newToken")
                        .build()
                } catch (e: Exception) {
                    return@runBlocking null
                }
            }
        }

        return response.request
    }

    private suspend fun refreshToken(): String? {
        val result = petfinderAuthApi.getToken()
        return result?.token
    }
}