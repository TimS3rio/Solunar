package com.timserio.solunar

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.android.gms.maps.model.LatLng
import com.timserio.core.R
import com.timserio.home_data.di.HomeDataModule
import com.timserio.home_domain.use_case.FormatDateForRequest
import com.timserio.home_domain.use_case.FormatSolunarTimes
import com.timserio.home_domain.use_case.GetDayRating
import com.timserio.home_domain.use_case.GetLocationName
import com.timserio.home_domain.use_case.GetSolunarTimes
import com.timserio.home_domain.use_case.GetTimezoneRegion
import com.timserio.home_domain.use_case.HomeUseCases
import com.timserio.home_presentation.HomeViewModel
import com.timserio.select_location_presentation.SelectLocationScreen
import com.timserio.select_location_presentation.SelectLocationState
import com.timserio.select_location_presentation.SelectLocationViewModel
import com.timserio.solunar.location.GeocodeLocationFake
import com.timserio.solunar.location.LocationTrackerFake
import com.timserio.solunar.repository.HomeRepositoryFake
import com.timserio.solunar.ui.theme.SolunarTheme
import com.timserio.test_utils.TestConstants
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(HomeDataModule::class)
class SelectLocationScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createComposeRule()

    private lateinit var selectLocationViewModel: SelectLocationViewModel
    private lateinit var homeRepositoryFake: HomeRepositoryFake
    private lateinit var locationTrackerFake: LocationTrackerFake
    private lateinit var geocodeLocationFake: GeocodeLocationFake
    private lateinit var homeUseCases: HomeUseCases
    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setup() {
        selectLocationViewModel = SelectLocationViewModel()
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
    fun noLocationSelectedTest() {
        composeRule.setContent {
            SolunarTheme {
                SelectLocationScreen(
                    latLong = Pair(TestConstants.LATITUDE, TestConstants.LONGITUDE),
                    state = SelectLocationState(
                        isLocationSelected = false,
                        selectedLocation = null
                    ),
                    onEvent = { selectLocationViewModel.state.handleEvent(it) },
                    onLocationSelected = {}
                )
            }
        }

        composeRule
            .onNodeWithText(UITestingUtil.getResourceString(R.string.select_location_msg))
            .assertExists()
    }

    @Test
    fun locationSelectedTest() {
        composeRule.setContent {
            SolunarTheme {
                SelectLocationScreen(
                    latLong = Pair(TestConstants.LATITUDE, TestConstants.LONGITUDE),
                    state = SelectLocationState(
                        isLocationSelected = true,
                        selectedLocation = LatLng(TestConstants.LATITUDE, TestConstants.LONGITUDE)
                    ),
                    onEvent = { selectLocationViewModel.state.handleEvent(it) },
                    onLocationSelected = {}
                )
            }
        }

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.select_location))
            .assertExists()

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.cancel))
            .assertExists()
    }

    @Test
    fun selectLocationFromMapAndClickCancel() {
        composeRule.setContent {
            SolunarTheme {
                SelectLocationScreen(
                    latLong = Pair(TestConstants.LATITUDE, TestConstants.LONGITUDE),
                    state = SelectLocationState(
                        isLocationSelected = true,
                        selectedLocation = LatLng(TestConstants.LATITUDE, TestConstants.LONGITUDE)
                    ),
                    onEvent = { selectLocationViewModel.state.handleEvent(it) },
                    onLocationSelected = {}
                )
            }
        }

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.cancel))
            .performClick()

        composeRule
            .onNodeWithText(UITestingUtil.getResourceString(R.string.select_location_msg))
            .assertExists()
    }
}
