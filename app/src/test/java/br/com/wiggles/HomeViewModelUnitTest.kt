package br.com.wiggles

import br.com.wiggles.data.PetResponse
import br.com.wiggles.data.PetsResponse
import br.com.wiggles.data.PetfinderNetwork
import br.com.wiggles.data.toPet
import br.com.wiggles.presentation.home.NearbyUiState
import br.com.wiggles.presentation.home.HomeViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelUnitTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: HomeViewModel
    private val petfinderNetwork: PetfinderNetwork by lazy { mockk() }

    @Test
    fun homeUiState_whenInitialized_shouldBeLoading() = runTest {
        mockSuccess()
        assertEquals(NearbyUiState.Loading, viewModel.homeUiState.value)
    }

    @Test
    fun homeUiState_whenFetchPetsSucceed_shouldBeSuccess() = runTest {
        val petsResponse = PetResponse.fakeList()
        val pets = petsResponse.map { it.toPet() }

        mockSuccess(networkResponse = PetsResponse(animals = petsResponse))
        advanceUntilIdle()

        assertEquals(NearbyUiState.Success(pets), viewModel.homeUiState.value)
    }

    @Test
    fun homeUiState_whenFetchPetsFails_shouldBeError() = runTest {
        mockError()
        assertEquals(NearbyUiState.Error, viewModel.homeUiState.value)
    }

    private fun mockSuccess(
        networkResponse: PetsResponse = PetsResponse(listOf())
    ) {
        coEvery {
            petfinderNetwork.getPets()
        } coAnswers {
            delay(1000)
            networkResponse
        }

        viewModel = HomeViewModel(
            petfinderNetwork = petfinderNetwork
        )
    }

    private fun mockError() {
        coEvery {
            petfinderNetwork.getPets()
        } throws Exception()

        viewModel = HomeViewModel(
            petfinderNetwork = petfinderNetwork
        )
    }

}