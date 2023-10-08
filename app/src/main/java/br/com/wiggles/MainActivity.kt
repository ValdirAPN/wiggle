package br.com.wiggles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.wiggles.presentation.MainViewModel
import br.com.wiggles.presentation.home.HomeRoute
import br.com.wiggles.presentation.petdetails.PetDetailsRoute
import br.com.wiggles.presentation.petdetails.PetDetailsViewModel
import br.com.wiggles.ui.theme.WigglesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var darkTheme by remember { mutableStateOf(false) }
            val token by mainViewModel.token.collectAsStateWithLifecycle()

            val navController = rememberNavController()

            WigglesTheme(
                darkTheme = darkTheme
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (token != null) {
                        NavHost(navController = navController, startDestination = "home") {
                            composable("home") {
                                HomeRoute(
                                    username = "Jon Doe",
                                    darkTheme = darkTheme,
                                    onSwitchTheme = { darkTheme = !darkTheme },
                                    onClickPetItem = { petId -> navController.navigate("petDetails/$petId") }
                                )
                            }
                            composable("petDetails/{petId}") {
                                PetDetailsRoute(
                                    onNavigateUp = { navController.navigateUp() }
                                )
                            }
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