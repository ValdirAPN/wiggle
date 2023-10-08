package br.com.wiggles.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.wiggles.data.PetfinderAuthNetwork
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val petfinderAuthNetwork: PetfinderAuthNetwork,
) : ViewModel() {

    private val _token: MutableStateFlow<String?> = MutableStateFlow(null)
    val token: StateFlow<String?> = _token.asStateFlow()

    init {
        viewModelScope.launch {
            val result = petfinderAuthNetwork.getToken()
            if (result != null) {
                _token.update { result.token }
            }
        }
    }
}