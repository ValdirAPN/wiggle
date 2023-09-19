package br.com.wiggles.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.wiggles.data.PetfinderNetwork
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {

    private val _mainUiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState.Loading)
    val mainUiState: StateFlow<MainUiState> = _mainUiState.asStateFlow()

    fun getToken() {
        val petfinderNetwork = PetfinderNetwork()

        viewModelScope.launch {
            val result = petfinderNetwork.getToken()
            if (result != null) {
                _mainUiState.update { MainUiState.Success(token = result.token) }
            } else {
                _mainUiState.update { MainUiState.Error }
            }
            Log.d(TAG, "getToken: $result")
        }
    }
}

sealed interface MainUiState {
    object Loading : MainUiState
    data class Success(val token: String) : MainUiState
    object Error : MainUiState
}