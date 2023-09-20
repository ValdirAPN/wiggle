package br.com.wiggles.presentation.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.wiggles.R
import br.com.wiggles.domain.model.Animal
import br.com.wiggles.domain.model.Gender
import br.com.wiggles.ui.theme.Blue60
import br.com.wiggles.ui.theme.Red60
import br.com.wiggles.ui.theme.WigglesTheme
import coil.compose.AsyncImage

const val NEARBY_RESULTS_LIST_TEST_TAG = "nearby_results_list_test_tag"
const val NEARBY_RESULT_ITEM_TEST_TAG = "nearby_result_item_test_tag"
const val NEARBY_RESULTS_EMPTY_TEST_TAG = "nearby_results_empty_test_tag"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    username: String = "",
    darkTheme: Boolean = false,
    onSwitchTheme: () -> Unit = {},
    nearbyResults: List<Animal> = listOf()
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HomeHeader(
            username = username,
            darkTheme = darkTheme,
            onSwitchTheme = onSwitchTheme
        )

        LazyColumn(
            modifier = Modifier.testTag(NEARBY_RESULTS_LIST_TEST_TAG),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            stickyHeader {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(bottom = 8.dp),
                    text = stringResource(id = R.string.nearby_results),
                    style = MaterialTheme.typography.titleSmall
                )
            }

            if (nearbyResults.isEmpty()) {
                item {
                    NearbyResultsEmpty()
                }
            } else {
                items(
                    key = { item -> item.id },
                    items = nearbyResults
                ) { animal ->
                    NearbyAnimal(
                        modifier = Modifier.testTag(NEARBY_RESULT_ITEM_TEST_TAG),
                        animal = animal
                    )
                }
            }

        }
    }
}

@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
    username: String,
    darkTheme: Boolean,
    onSwitchTheme: () -> Unit
) {
    val greetingText = stringResource(id = R.string.greeting, username)
    val message = stringResource(id = R.string.home_header_message)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column {
            Text(text = greetingText, style = MaterialTheme.typography.titleLarge)
            Text(text = message)
        }

        IconButton(
            modifier = Modifier.testTag("theme_switch_button"),
            onClick = { onSwitchTheme() }
        ) {
            val icon = if (darkTheme) {
                painterResource(id = R.drawable.ligth_off)
            } else {
                painterResource(
                    id = R.drawable.light_on
                )
            }

            val description = if (darkTheme) {
                stringResource(id = R.string.switch_to_light_theme)
            } else {
                stringResource(id = R.string.switch_to_dark_theme)
            }

            Icon(
                painter = icon,
                contentDescription = description
            )
        }
    }
}

@Composable
fun NearbyAnimal(
    modifier: Modifier = Modifier,
    animal: Animal
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = Color.Unspecified
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                model = animal.imageUrl,
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.paw),
                error = painterResource(id = R.drawable.paw),

            )
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = animal.name.lowercase().replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Normal),
                        color = MaterialTheme.colorScheme.onSurface,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )

                    val (gender, color) = when (animal.gender) {
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

                val age = stringResource(id = R.string.years, animal.age)
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = age,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    modifier = Modifier.padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val distance = stringResource(id = R.string.distance_from, animal.distance)

                    Icon(
                        painter = painterResource(id = R.drawable.pin),
                        contentDescription = stringResource(
                            id = R.string.distance
                        )
                    )
                    Text(text = distance, color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}

@Preview
@Composable
fun NearbyAnimalPreview() {
    WigglesTheme {
        Surface {
            val animal = Animal.fakeList().first()
            NearbyAnimal(animal = animal)
        }
    }
}

@Composable
fun NearbyResultsEmpty() {
    Column(modifier = Modifier.testTag(NEARBY_RESULTS_EMPTY_TEST_TAG)) {
        Text(text = stringResource(id = R.string.no_results_found))
    }
}
