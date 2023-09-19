package br.com.wiggles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.wiggles.domain.model.Animal
import br.com.wiggles.presentation.MainUiState
import br.com.wiggles.presentation.MainViewModel
import br.com.wiggles.presentation.home.HomeScreen
import br.com.wiggles.ui.theme.WigglesTheme

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel.getToken()

        setContent {
            var darkTheme by remember { mutableStateOf(false) }
            val mainUiState by mainViewModel.mainUiState.collectAsStateWithLifecycle()

            WigglesTheme(
                darkTheme = darkTheme
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (mainUiState) {
                        MainUiState.Loading -> {
                            Column(
                                Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        MainUiState.Error -> {}

                        else -> {
                            HomeScreen(
                                username = "Spikey",
                                darkTheme = darkTheme,
                                onSwitchTheme = { darkTheme = !darkTheme },
                                nearbyResults = Animal.fakeList()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WigglesTheme {
        Greeting("Android")
    }
}