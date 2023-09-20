package br.com.wiggles

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import br.com.wiggles.domain.model.Animal
import br.com.wiggles.domain.model.Gender
import br.com.wiggles.presentation.home.HomeScreen
import br.com.wiggles.presentation.home.NEARBY_RESULTS_LIST_TEST_TAG
import br.com.wiggles.presentation.home.NEARBY_RESULT_ITEM_TEST_TAG
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class HomeScreenInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun header_whenLoaded_shouldDisplayGreetings() {
        with(composeTestRule) {
            val username = "Spikey"

            activity.setContent {
                HomeScreen(
                    username = username
                )
            }

            val greetingText = activity.getString(R.string.greeting, username)

            val message = activity.getString(R.string.home_header_message)

            onNodeWithText(greetingText).assertExists()
            onNodeWithText(message).assertExists()
        }
    }

    @Test
    fun header_shouldDisplayThemeSwitchButton() {
        with(composeTestRule) {
            onNodeWithTag("theme_switch_button")
                .assertExists()

            val switchToDarkThemeContentDescription =
                activity.getString(R.string.switch_to_dark_theme)

            onNodeWithContentDescription(switchToDarkThemeContentDescription)
                .assertExists()
        }
    }

    @Test
    fun theme_default_shouldBeLight() {
        with(composeTestRule) {
            val switchToDarkThemeContentDescription =
                activity.getString(R.string.switch_to_dark_theme)

            onNodeWithContentDescription(switchToDarkThemeContentDescription)
                .assertExists()
        }
    }

    @Test
    fun themeSwitchButton_whenClicked_shouldSwitchTheme() {
        with(composeTestRule) {
            val switchToDarkThemeContentDescription =
                activity.getString(R.string.switch_to_dark_theme)

            val switchToLightThemeContentDescription =
                activity.getString(R.string.switch_to_light_theme)

            onNodeWithTag("theme_switch_button")
                .performClick()

            onNodeWithContentDescription(switchToLightThemeContentDescription)
                .assertExists()

            onNodeWithTag("theme_switch_button")
                .performClick()

            onNodeWithContentDescription(switchToDarkThemeContentDescription)
                .assertExists()
        }
    }

    @Test
    fun home_whenEmptyNearbyResults_shouldDisplayNoItems() {
        with(composeTestRule) {
            activity.setContent {
                HomeScreen(nearbyResults = listOf())
            }

            onNodeWithTag(NEARBY_RESULTS_LIST_TEST_TAG)
                .onChildren()
                .filter(hasTestTag(NEARBY_RESULT_ITEM_TEST_TAG))
                .assertCountEquals(0)
        }
    }

    @Test
    fun home_whenEmptyNearbyResults_shouldDisplayNoItemsFoundMessage() {
        with(composeTestRule) {
            activity.setContent {
                HomeScreen(nearbyResults = listOf())
            }

            val emptyResultMessage = activity.getString(R.string.no_results_found)

            onNodeWithText(emptyResultMessage).assertExists()
        }
    }

    @Test
    fun home_whenLoaded_shouldDisplayNearbyResultsTitle() {
        with(composeTestRule) {
            val nearbyResultsTitle = activity.getString(R.string.nearby_results)

            onNodeWithText(nearbyResultsTitle)
                .assertExists()
        }
    }

    @Test
    fun home_whenLoaded_shouldDisplayNearbyResultsItems() {
        with(composeTestRule) {
            val itemsCount = 3
            val nearbyResults = Animal.fakeList().take(itemsCount)

            activity.setContent {
                HomeScreen(nearbyResults = nearbyResults)
            }

            onNodeWithTag(NEARBY_RESULTS_LIST_TEST_TAG)
                .onChildren()
                .filter(hasTestTag(NEARBY_RESULT_ITEM_TEST_TAG))
                .assertCountEquals(itemsCount)
        }
    }

    @Test
    fun nearbyResultItem_whenVisible_shouldDisplayAnimalGenderAgeAndDistance() {
        with(composeTestRule) {
            val animals = listOf(
                Animal(
                    id = 1,
                    name = "Marley",
                    age = 4,
                    gender = Gender.MALE,
                    distance = 1000L,
                    postedAt = LocalDateTime.now()
                ),
            )

            activity.setContent {
                HomeScreen(nearbyResults = animals)
            }

            val maleText = activity.getString(R.string.male)
            val ageText = activity.getString(R.string.years, 4)
            val distanceText = activity.getString(R.string.distance_from, 1000)

            onNodeWithText(maleText).assertExists()
            onNodeWithText(ageText).assertExists()
            onNodeWithText(distanceText).assertExists()
        }
    }
}