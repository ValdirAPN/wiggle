package br.com.wiggles.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.wiggles.data.PetfinderAuthNetwork
import br.com.wiggles.data.PetfinderNetwork
import br.com.wiggles.data.toAnimal
import br.com.wiggles.domain.model.Animal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainViewModel"

@HiltViewModel
class MainViewModel @Inject constructor(
    private val petfinderAuthNetwork: PetfinderAuthNetwork,
    private val petfinderNetwork: PetfinderNetwork
) : ViewModel() {

    private val _mainUiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState.Loading)
    val mainUiState: StateFlow<MainUiState> = _mainUiState.asStateFlow()

    private var token: String? = null

    init {
        viewModelScope.launch {
            val result = petfinderAuthNetwork.getToken()
            if (result != null) {
                token = result.token
            }

            getAnimals()
        }
    }

    private fun getAnimals() {
        viewModelScope.launch {
            if (token != null) {
                val result = petfinderNetwork.getAnimals()
                if (result != null) {
                    _mainUiState.update {
                        MainUiState.Success(
                            animals = result.animals.filter { it.photos.isNotEmpty() }.map { it.toAnimal() }
                        )
                    }
                } else {
                    _mainUiState.update { MainUiState.Error }
                }
            }
        }
    }
}

sealed interface MainUiState {
    object Loading : MainUiState
    data class Success(val animals: List<Animal>) : MainUiState
    object Error : MainUiState
}