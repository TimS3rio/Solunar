package com.timserio.solunar

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.timserio.core.R
import com.timserio.home_data.di.HomeDataModule
import com.timserio.home_domain.use_case.FormatDateForRequest
import com.timserio.home_domain.use_case.FormatSolunarTimes
import com.timserio.home_domain.use_case.GetDayRating
import com.timserio.home_domain.use_case.GetLocationName
import com.timserio.home_domain.use_case.GetSolunarTimes
import com.timserio.home_domain.use_case.GetTimezoneRegion
import com.timserio.home_domain.use_case.HomeUseCases
import com.timserio.home_presentation.HomeEvent
import com.timserio.home_presentation.HomeScreen
import com.timserio.home_presentation.HomeState
import com.timserio.home_presentation.HomeViewModel
import com.timserio.home_presentation.RequestLocationState
import com.timserio.solunar.location.GeocodeLocationFake
import com.timserio.solunar.location.LocationTrackerFake
import com.timserio.solunar.navigation.Route
import com.timserio.solunar.navigation.SolunarNavHost
import com.timserio.solunar.repository.HomeRepositoryFake
import com.timserio.solunar.ui.theme.SolunarTheme
import com.timserio.test_utils.TestConstants
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(HomeDataModule::class)
class HomeScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createComposeRule()
    lateinit var navController: TestNavHostController

    private lateinit var homeRepositoryFake: HomeRepositoryFake
    private lateinit var locationTrackerFake: LocationTrackerFake
    private lateinit var geocodeLocationFake: GeocodeLocationFake
    private lateinit var homeUseCases: HomeUseCases
    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setup() {
        homeRepositoryFake = HomeRepositoryFake()
        geocodeLocationFake = GeocodeLocationFake()
        homeUseCases = HomeUseCases(
            FormatDateForRequest(),
            FormatSolunarTimes(),
            GetSolunarTimes(homeRepositoryFake),
            GetTimezoneRegion(),
            GetLocationName(),
            GetDayRating()
        )
        locationTrackerFake = LocationTrackerFake()
        homeViewModel = HomeViewModel(homeUseCases, locationTrackerFake, geocodeLocationFake)
    }

    @Test
    fun getSolunarTimesSuccessTest() {
        composeRule.setContent {
            SolunarTheme {
                HomeScreen(
                    HomeState(
                        isLoading = false,
                        latLong = Pair(TestConstants.LATITUDE, TestConstants.LONGITUDE),
                        timeZone = TestConstants.TIMEZONE,
                        locationName = TestConstants.MINOCQUA_WI,
                        majorOne = TestConstants.FORMATTED_MAJOR_ONE,
                        majorTwo = TestConstants.FORMATTED_MAJOR_TWO,
                        minorOne = TestConstants.FORMATTED_MINOR_ONE,
                        minorTwo = TestConstants.FORMATTED_MINOR_TWO,
                        dayRating = Pair(80, .8f),
                        requestLocationState = RequestLocationState.LOCATION_REQUEST_SUCCESSFUL,
                        isSolunarResponseSuccessful = true
                    ),
                    getTestPermissionLauncher(),
                    {},
                    { event -> homeViewModel.state.handleEvent(event) },
                    null
                )
            }
        }

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.location_name))
            .assertTextEquals(TestConstants.MINOCQUA_WI)

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.date))
            .assertTextEquals(UITestingUtil.getResourceString(R.string.today))

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.major_one))
            .assertTextEquals(TestConstants.FORMATTED_MAJOR_ONE)

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.major_two))
            .assertTextEquals(TestConstants.FORMATTED_MAJOR_TWO)

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.minor_one))
            .assertTextEquals(TestConstants.FORMATTED_MINOR_ONE)

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.minor_two))
            .assertTextEquals(TestConstants.FORMATTED_MINOR_TWO)

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.day_rating))
            .assertTextEquals(TestConstants.DAY_RATING_VALUE)

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.edit_location))
            .assertExists()

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.error_text))
            .assertDoesNotExist()
    }

    @Test
    fun getSolunarTimesFailureTest() {
        composeRule.setContent {
            SolunarTheme {
                HomeScreen(
                    HomeState(
                        isLoading = false,
                        latLong = Pair(TestConstants.LATITUDE, TestConstants.LONGITUDE),
                        requestLocationState = RequestLocationState.LOCATION_REQUEST_SUCCESSFUL,
                        isSolunarResponseSuccessful = false
                    ),
                    getTestPermissionLauncher(),
                    {},
                    { event -> homeViewModel.state.handleEvent(event) },
                    null
                )
            }
        }

        val errorMsg = UITestingUtil.getResourceString(R.string.error_msg)
        composeRule
            .onNodeWithContentDescription(errorMsg)
            .assertTextEquals(errorMsg)

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.select_a_location))
            .assertExists()

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.get_current_location))
            .assertExists()

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.edit_location))
            .assertDoesNotExist()

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.loading))
            .assertDoesNotExist()

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.location_name))
            .assertDoesNotExist()
    }

    @Test
    fun getSolunarTimesLoadingTest() {
        composeRule.setContent {
            SolunarTheme {
                HomeScreen(
                    HomeState(
                        isLoading = true,
                        requestLocationState = RequestLocationState.LOCATION_REQUEST_SUCCESSFUL,
                        isSolunarResponseSuccessful = null
                    ),
                    getTestPermissionLauncher(),
                    {},
                    { event -> homeViewModel.state.handleEvent(event) },
                    null
                )
            }
        }

        val nodes = composeRule
            .onAllNodesWithContentDescription(UITestingUtil.getResourceString(R.string.loading))

        for (i in 0..5) {
            nodes.onFirst().assertExists()
        }

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.edit_location))
            .assertExists()

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.location_name))
            .assertDoesNotExist()

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.error_text))
            .assertDoesNotExist()
    }

    @Test
    fun getLocationFailedTest() {
        composeRule.setContent {
            SolunarTheme {
                HomeScreen(
                    HomeState(requestLocationState = RequestLocationState.LOCATION_REQUEST_FAILED),
                    getTestPermissionLauncher(),
                    {},
                    { event -> homeViewModel.state.handleEvent(event) },
                    null
                )
            }
        }

        val noLocationMsg = UITestingUtil.getResourceString(R.string.no_location_permission_msg)
        composeRule
            .onNodeWithContentDescription(noLocationMsg)
            .assertTextEquals(noLocationMsg)

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.select_a_location))
            .assertExists()

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.get_current_location))
            .assertExists()

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.edit_location))
            .assertDoesNotExist()

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.loading))
            .assertDoesNotExist()

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.location_name))
            .assertDoesNotExist()
    }

    @Test
    fun noLocationSetTest() {
        composeRule.setContent {
            SolunarTheme {
                HomeScreen(
                    HomeState(),
                    getTestPermissionLauncher(),
                    {},
                    { event -> homeViewModel.state.handleEvent(event) },
                    null
                )
            }
        }

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.get_location_prompt))
            .assertExists()

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.select_a_location))
            .assertExists()

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.get_current_location))
            .assertExists()

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.edit_location))
            .assertDoesNotExist()
    }

    @Test
    fun selectLocationClickTest() {
        composeRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            SolunarTheme {
                SolunarNavHost(navController = navController, homeViewModel = homeViewModel, permissionResultLauncher = getTestPermissionLauncher())
            }
        }

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.select_a_location))
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route
        Assert.assertEquals(Route.SELECT_LOCATION, route)
    }

    @Composable
    private fun getTestPermissionLauncher(): ActivityResultLauncher<String> {
        return rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = {
                homeViewModel.state.handleEvent(HomeEvent.OnLocationGranted)
            }
        )
    }
}
