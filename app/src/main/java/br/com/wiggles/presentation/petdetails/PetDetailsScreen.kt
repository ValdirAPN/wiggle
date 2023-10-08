package br.com.wiggles.presentation.petdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.wiggles.R
import br.com.wiggles.domain.model.Gender
import br.com.wiggles.domain.model.Pet
import br.com.wiggles.ui.theme.Blue60
import br.com.wiggles.ui.theme.Red60
import br.com.wiggles.ui.theme.WigglesTheme
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDetailsRoute(
    viewModel: PetDetailsViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
) {
    val petUiState by viewModel.petUiState.collectAsStateWithLifecycle()

    val lazyListState = rememberLazyListState()
    var scrolledY = 0f
    var previousOffset = 0

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        state = lazyListState
    ) {
        item {
            TopAppBar(
                modifier = Modifier
                    .graphicsLayer {
                        scrolledY += lazyListState.firstVisibleItemScrollOffset - previousOffset
                        translationY = scrolledY * 0.5f
                        previousOffset = lazyListState.firstVisibleItemScrollOffset
                    },
                title = { Text(text = "Pet details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        item {
            when (val state = petUiState) {
                PetUiState.Loading -> {
                    Column(
                        Modifier.fillParentMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }

                PetUiState.Error -> {
                    Text(text = "Error")
                }

                is PetUiState.Success -> {
                    PetDetailsScreen(state.pet)
                }
            }
        }
    }
}

@Composable
fun PetDetailsScreen(pet: Pet) {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp),
            contentScale = ContentScale.Crop,
            model = pet.petImage?.fullImageUrl,
            contentDescription = null,
            placeholder = painterResource(id = R.drawable.paw),
            error = painterResource(id = R.drawable.paw),
        )
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        text = pet.name,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
                    )

                    val (gender, color) = when (pet.gender) {
                        Gender.MALE -> Pair(stringResource(id = R.string.male), Blue60)
                        Gender.FEMALE -> Pair(stringResource(id = R.string.female), Red60)
                    }
                    Text(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(color.copy(alpha = 0.1f))
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        text = gender,
                        color = color
                    )
                }
            }

            pet.description?.let {
                AboutMe(description = it)
            }

            QuickInfo(pet = pet)
        }
    }
}

@Composable
fun AboutMe(description: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(id = R.string.about_me),
            style = MaterialTheme.typography.titleLarge
        )
        Text(text = description)
    }
}

@Composable
fun QuickInfo(pet: Pet) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.quick_info),
            style = MaterialTheme.typography.titleLarge
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickInfoItem(modifier = Modifier.weight(1f), label = "Age", value = pet.age)
            pet.color?.let {
                QuickInfoItem(modifier = Modifier.weight(1f), label = "Color", value = it)
            }
            QuickInfoItem(modifier = Modifier.weight(1f), label = "Size", value = pet.size)
        }
    }
}

@Composable
fun QuickInfoItem(modifier: Modifier = Modifier, label: String, value: String) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = value, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}

@Preview
@Composable
fun PetDetailsScreenPreview() {
    WigglesTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            PetDetailsScreen(pet = Pet.fakeList().first())
        }
    }
}
