package br.com.wiggles.presentation.petdetails

import androidx.lifecycle.SavedStateHandle
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

@HiltViewModel
class PetDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val petfinderNetwork: PetfinderNetwork
) : ViewModel() {

    private val petId: String = checkNotNull(savedStateHandle["petId"])

    private val _petUiState: MutableStateFlow<PetUiState> = MutableStateFlow(PetUiState.Loading)
    val petUiState: StateFlow<PetUiState> = _petUiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                petfinderNetwork.getPet(petId = petId).let { petResponse ->
                    petResponse?.toPet()?.let { pet ->
                        _petUiState.update {
                            PetUiState.Success(pet = pet)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _petUiState.update { PetUiState.Error }
            }
        }
    }
}

sealed interface PetUiState {
    object Loading : PetUiState
    data class Success(val pet: Pet) : PetUiState
    object Error : PetUiState
}