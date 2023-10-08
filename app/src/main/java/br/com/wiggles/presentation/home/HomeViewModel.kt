package br.com.wiggles.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.wiggles.data.PetfinderNetwork
import br.com.wiggles.data.toPet
import br.com.wiggles.domain.model.Pet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainViewModel"

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val petfinderNetwork: PetfinderNetwork
) : ViewModel() {

    private val _nearbyUiState: MutableStateFlow<NearbyUiState> = MutableStateFlow(NearbyUiState.Loading)
    val homeUiState: StateFlow<NearbyUiState> = _nearbyUiState.asStateFlow()

    init {
        getPets()
    }
    private fun getPets() {
        viewModelScope.launch {
            try {
                val result = petfinderNetwork.getPets()
                if (result != null) {
                    _nearbyUiState.update {
                        NearbyUiState.Success(
                            pets = result.animals.map { it.toPet() }
                        )
                    }
                } else {
                    _nearbyUiState.update { NearbyUiState.Error }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _nearbyUiState.update { NearbyUiState.Error }
            }
        }
    }
}

sealed interface NearbyUiState {
    object Loading : NearbyUiState
    data class Success(val pets: List<Pet>) : NearbyUiState
    object Error : NearbyUiState
}