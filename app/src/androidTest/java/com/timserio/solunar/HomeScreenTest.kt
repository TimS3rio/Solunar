package com.timserio.solunar

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.platform.app.InstrumentationRegistry
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.timserio.core.R
import com.timserio.home_domain.use_case.FormatDateForRequest
import com.timserio.home_domain.use_case.FormatSolunarTimes
import com.timserio.home_domain.use_case.GetDayRating
import com.timserio.home_domain.use_case.GetLocationName
import com.timserio.home_domain.use_case.GetSolunarTimes
import com.timserio.home_domain.use_case.GetTimezoneRegion
import com.timserio.home_domain.use_case.HomeUseCases
import com.timserio.home_presentation.HomeScreen
import com.timserio.home_presentation.HomeState
import com.timserio.home_presentation.HomeViewModel
import com.timserio.solunar.location.LocationTrackerFake
import com.timserio.solunar.repository.HomeRepositoryFake
import com.timserio.solunar.ui.theme.SolunarTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalPermissionsApi
@HiltAndroidTest
class HomeScreenTest {

    companion object {
        private const val FORMATTED_MAJOR_ONE = "12:00 AM - 2:00 AM"
        private const val FORMATTED_MAJOR_TWO = "1:15 PM - 3:15 PM"
        private const val FORMATTED_MINOR_ONE = "10:59 PM - 11:59 PM"
        private const val FORMATTED_MINOR_TWO = "8:30 AM - 9:30 AM"
        private const val LATITUDE = "45.8711"
        private const val LONGITUDE = "-89.7093"
        private const val TIMEZONE = -5
        private const val LOCATION_NAME = "Minocqua, WI"
        private const val DAY_RATING_VALUE = "80"
    }

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createComposeRule()

    private lateinit var homeRepositoryFake: HomeRepositoryFake
    private lateinit var locationTrackerFake: LocationTrackerFake
    private lateinit var homeUseCases: HomeUseCases
    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setup() {
        homeRepositoryFake = HomeRepositoryFake()
        homeUseCases = HomeUseCases(
            FormatDateForRequest(),
            FormatSolunarTimes(),
            GetSolunarTimes(homeRepositoryFake),
            GetTimezoneRegion(),
            GetLocationName(),
            GetDayRating()
        )
        locationTrackerFake = LocationTrackerFake()
        homeViewModel = HomeViewModel(homeUseCases, locationTrackerFake)
    }

    @Test
    fun getSolunarTimesSuccessTest() {
        composeRule.setContent {
            SolunarTheme {
                HomeScreen(
                    HomeState(
                        isLoading = false,
                        latLong = Pair(LATITUDE, LONGITUDE),
                        timeZone = TIMEZONE,
                        locationName = LOCATION_NAME,
                        majorOne = FORMATTED_MAJOR_ONE,
                        majorTwo = FORMATTED_MAJOR_TWO,
                        minorOne = FORMATTED_MINOR_ONE,
                        minorTwo = FORMATTED_MINOR_TWO,
                        dayRating = Pair(80, .8f),
                        isLocationRequestSuccessful = true,
                        isSolunarResponseSuccessful = true
                    )
                ) {
                    homeViewModel.onEvent(it)
                }
            }
        }

        composeRule
            .onNodeWithContentDescription(getResourceString(R.string.location_name))
            .assertTextEquals(LOCATION_NAME)

        composeRule
            .onNodeWithContentDescription(getResourceString(R.string.date))
            .assertTextEquals(getResourceString(R.string.today))

        composeRule
            .onNodeWithContentDescription(getResourceString(R.string.major_one))
            .assertTextEquals(FORMATTED_MAJOR_ONE)

        composeRule
            .onNodeWithContentDescription(getResourceString(R.string.major_two))
            .assertTextEquals(FORMATTED_MAJOR_TWO)

        composeRule
            .onNodeWithContentDescription(getResourceString(R.string.minor_one))
            .assertTextEquals(FORMATTED_MINOR_ONE)

        composeRule
            .onNodeWithContentDescription(getResourceString(R.string.minor_two))
            .assertTextEquals(FORMATTED_MINOR_TWO)

        composeRule
            .onNodeWithContentDescription(getResourceString(R.string.day_rating))
            .assertTextEquals(DAY_RATING_VALUE)

        composeRule
            .onNodeWithContentDescription(getResourceString(R.string.error_text))
            .assertDoesNotExist()
    }

    @Test
    fun getSolunarTimesFailureTest() {
        composeRule.setContent {
            SolunarTheme {
                HomeScreen(
                    HomeState(
                        isLoading = false,
                        latLong = Pair(LATITUDE, LONGITUDE),
                        isLocationRequestSuccessful = true,
                        isSolunarResponseSuccessful = false
                    )
                ) {
                    homeViewModel.onEvent(it)
                }
            }
        }

        composeRule
            .onNodeWithContentDescription(getResourceString(R.string.error_text))
            .assertTextEquals(getResourceString(R.string.error_msg))

        composeRule
            .onNodeWithContentDescription(getResourceString(R.string.loading))
            .assertDoesNotExist()

        composeRule
            .onNodeWithContentDescription(getResourceString(R.string.location_name))
            .assertDoesNotExist()
    }

    @Test
    fun getSolunarTimesLoadingTest() {
        composeRule.setContent {
            SolunarTheme {
                HomeScreen(
                    HomeState(
                        isLoading = true,
                        latLong = Pair(LATITUDE, LONGITUDE),
                        timeZone = TIMEZONE,
                        isLocationRequestSuccessful = true,
                        isSolunarResponseSuccessful = null
                    )
                ) {
                    homeViewModel.onEvent(it)
                }
            }
        }

        val nodes = composeRule
            .onAllNodesWithContentDescription(getResourceString(R.string.loading))
        for (i in 0..5) {
            nodes[i].assertExists()
        }

        composeRule
            .onNodeWithContentDescription(getResourceString(R.string.location_name))
            .assertDoesNotExist()

        composeRule
            .onNodeWithContentDescription(getResourceString(R.string.error_text))
            .assertDoesNotExist()
    }

    @Test
    fun getLocationFailedTest() {
        composeRule.setContent {
            SolunarTheme {
                HomeScreen(HomeState(isLocationRequestSuccessful = false)) {
                    homeViewModel.onEvent(it)
                }
            }
        }

        composeRule
            .onNodeWithContentDescription(getResourceString(R.string.error_text))
            .assertTextEquals(getResourceString(R.string.turn_on_location_desc))

        composeRule
            .onNodeWithContentDescription(getResourceString(R.string.loading))
            .assertDoesNotExist()

        composeRule
            .onNodeWithContentDescription(getResourceString(R.string.location_name))
            .assertDoesNotExist()
    }

    private fun getResourceString(id: Int): String {
        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        return targetContext.resources.getString(id)
    }
}
