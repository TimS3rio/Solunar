package com.timserio.home_presentation

import com.timserio.home_domain.location.GeocodeLocation
import com.timserio.home_domain.location.LocationData
import com.timserio.home_domain.location.LocationTracker
import com.timserio.home_domain.model.SolunarData
import com.timserio.home_domain.use_case.FormatDateForRequest
import com.timserio.home_domain.use_case.FormatSolunarTimes
import com.timserio.home_domain.use_case.GetDayRating
import com.timserio.home_domain.use_case.GetLocationName
import com.timserio.home_domain.use_case.GetSolunarTimes
import com.timserio.home_domain.use_case.GetTimezoneRegion
import com.timserio.home_domain.use_case.HomeUseCases
import com.timserio.test_utils.MainDispatcherRule
import com.timserio.test_utils.TestConstants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class HomeViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: HomeRepositoryFake
    private lateinit var useCases: HomeUseCases
    private lateinit var locationTracker: LocationTracker
    private lateinit var geocodeLocation: GeocodeLocation
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        repository = HomeRepositoryFake()
        useCases = HomeUseCases(
            FormatDateForRequest(),
            FormatSolunarTimes(),
            GetSolunarTimes(repository),
            GetTimezoneRegion(),
            GetLocationName(),
            GetDayRating()
        )
        locationTracker = LocationTrackerFake()
        geocodeLocation = GeocodeLocationFake()
        viewModel = HomeViewModel(
            useCases,
            locationTracker,
            geocodeLocation
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Handle loading event test`() = runTest {
        // WHEN: handleEvent(HomeEvent.OnLoading) is called
        viewModel.state.handleEvent(HomeEvent.OnLoading)

        val expected = HomeState(
            isLoading = true,
            requestLocationState = RequestLocationState.LOCATION_REQUEST_SUCCESSFUL
        )
        // THEN: Then isLoading is true and requestLocationState is LOCATION_REQUEST_SUCCESSFUL
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Handle previous day click event test`() = runTest {
        // GIVEN: State has date set to 12/21/23 and has location data set
        setLocationLoadedState(LocalDate.of(2023, 12, 21))

        // WHEN: handleEvent(HomeEvent.OnPreviousDayClick) is called
        viewModel.state.handleEvent(HomeEvent.OnPreviousDayClick)

        val expected = getExpectedState(LocalDate.of(2023, 12, 20))
        // THEN: state has date set to 12/20/23 and has solunar data set
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Handle next day click event test`() = runTest {
        // GIVEN: State has date set to 12/21/23 and has location data set
        setLocationLoadedState(LocalDate.of(2023, 12, 21))

        // WHEN: handleEvent(HomeEvent.OnNextDayClick) is called
        viewModel.state.handleEvent(HomeEvent.OnNextDayClick)

        val expected = getExpectedState(LocalDate.of(2023, 12, 22))
        // THEN: state has date set to 12/22/23 and has solunar data set
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Handle location granted event test`() = runTest {
        // WHEN: handleEvent(HomeEvent.OnLocationGranted) is called
        viewModel.state.handleEvent(HomeEvent.OnLocationGranted)

        val expected = getExpectedState(LocalDate.now())
        // THEN: state has requestLocationState set to LOCATION_REQUEST_SUCCESSFUL and has solunar data set
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Handle dismiss rationale event test`() = runTest {
        // WHEN: handleEvent(HomeEvent.OnDismissRationale) is called
        viewModel.state.handleEvent(HomeEvent.OnDismissRationale)

        // THEN: state has showPermissionRationale set to false
        Assert.assertEquals(HomeState(showPermissionRationale = false), viewModel.state.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Handle dismiss go to app settings event test`() = runTest {
        // WHEN: handleEvent(HomeEvent.OnDismissGoToAppSettings) is called
        viewModel.state.handleEvent(HomeEvent.OnDismissGoToAppSettings)

        // THEN: state has showPermissionRationale set to false
        Assert.assertEquals(HomeState(showPermissionRationale = false), viewModel.state.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Handle permission result event test`() = runTest {
        // WHEN: handleEvent(HomeEvent.OnPermissionResult(true)) is called
        viewModel.state.handleEvent(HomeEvent.OnPermissionResult(true))

        // THEN: state has not been updated
        Assert.assertEquals(HomeState(), viewModel.state.value)

        // WHEN: handleEvent(HomeEvent.OnPermissionResult(false)) is called
        viewModel.state.handleEvent(HomeEvent.OnPermissionResult(false))

        // THEN: state has not been updated
        Assert.assertEquals(HomeState(showPermissionRationale = true), viewModel.state.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Handle set date event test`() = runTest {
        // WHEN: handleEvent(HomeEvent.OnDismissGoToAppSettings) is called
        val expectedDate = LocalDate.now()
        viewModel.state.handleEvent(HomeEvent.OnSetDate(expectedDate))

        // THEN: state has date set to the expected date
        Assert.assertEquals(expectedDate, viewModel.state.value.date)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Handle current location loaded event test`() = runTest {
        // GIVEN: state has date set to 12/21/23
        val date = LocalDate.of(2023, 12, 21)
        viewModel.state.handleEvent(HomeEvent.OnSetDate(date))

        // WHEN: handleEvent(HomeEvent.OnCurrentLocationLoaded) is called
        val locationData = getLocationData()
        viewModel.state.handleEvent(HomeEvent.OnCurrentLocationLoaded(locationData))

        // THEN: state has requestLocationState set to LOCATION_REQUEST_SUCCESSFUL and has solunar data set
        val expectedState = getExpectedState(date)
        Assert.assertEquals(expectedState, viewModel.state.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Handle current location loaded event with network error test`() = runTest {
        // GIVEN: state has date set to 12/21/23
        val date = LocalDate.of(2023, 12, 21)
        viewModel.state.handleEvent(HomeEvent.OnSetDate(date))
        repository.shouldReturnError = true

        // WHEN: handleEvent(HomeEvent.OnCurrentLocationLoaded) is called
        val locationData = getLocationData()
        viewModel.state.handleEvent(HomeEvent.OnCurrentLocationLoaded(locationData))

        // THEN: state has requestLocationState set to LOCATION_REQUEST_SUCCESSFUL and isSolunarResponseSuccessful set to false
        Assert.assertEquals(
            HomeState(
                isLoading = false,
                latLong = Pair(TestConstants.LATITUDE, TestConstants.LONGITUDE),
                locationName = TestConstants.MINOCQUA_WI,
                date = date,
                requestLocationState = RequestLocationState.LOCATION_REQUEST_SUCCESSFUL,
                isSolunarResponseSuccessful = false
            ),
            viewModel.state.value
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Handle solunar times loaded event test`() = runTest {
        // GIVEN: state has date set to 12/21/23
        val date = LocalDate.of(2023, 12, 21)
        setLocationLoadedState(date)

        // WHEN: handleEvent(HomeEvent.OnSolunarTimesLoaded) is called
        val solunarData = getSolunarData()
        viewModel.state.handleEvent(HomeEvent.OnSolunarTimesLoaded(solunarData))

        // THEN: state has requestLocationState set to LOCATION_REQUEST_SUCCESSFUL and has solunar data set
        val expectedState = getExpectedState(date)
        Assert.assertEquals(expectedState, viewModel.state.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Handle location selected from map event test`() = runTest {
        // GIVEN: state has date set to 12/21/23
        val date = LocalDate.of(2023, 12, 21)
        setLocationLoadedState(date)

        // WHEN: handleEvent(HomeEvent.OnLocationSelectedFromMap) is called
        viewModel.state.handleEvent(
            HomeEvent.OnLocationSelectedFromMap(Pair(TestConstants.LATITUDE, TestConstants.LONGITUDE))
        )

        // THEN: state has solunar data set
        val expectedState = getExpectedState(date)
        Assert.assertEquals(expectedState, viewModel.state.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Handle location from map geocoded event test`() = runTest {
        // GIVEN: state has date set to 12/21/23
        val date = LocalDate.of(2023, 12, 21)
        setLocationLoadedState(date)

        // WHEN: handleEvent(HomeEvent.OnLocationFromMapGeocoded) is called
        val locationData = getLocationData()
        viewModel.state.handleEvent(HomeEvent.OnLocationFromMapGeocoded(locationData))

        // THEN: state has locationName set to Minocqua, WI and has solunar data set
        val expectedState = getExpectedState(date)
        Assert.assertEquals(expectedState, viewModel.state.value)
    }

    private fun getSolunarData(): SolunarData {
        return SolunarData(
            TestConstants.MAJOR_ONE_START,
            TestConstants.MAJOR_ONE_END,
            TestConstants.MAJOR_TWO_START,
            TestConstants.MAJOR_TWO_END,
            TestConstants.MINOR_ONE_START,
            TestConstants.MINOR_ONE_END,
            TestConstants.MINOR_TWO_START,
            TestConstants.MINOR_TWO_END,
            listOf(
                50, 60, 50, 20, 10, 0, 0, 10,
                20, 20, 20, 30, 0, 60, 70, 60,
                40, 30, 0, 0, 0, 10, 0, 0,
                0, 0, 10, 20, 20, 10, 0, 0
            )
        )
    }

    private fun setLocationLoadedState(date: LocalDate) {
        viewModel.state.handleEvent(HomeEvent.OnSetDate(date))
        val expectedLocationData = getLocationData()
        viewModel.state.handleEvent(HomeEvent.OnCurrentLocationLoaded(expectedLocationData))
    }

    private fun getLocationData(): LocationData {
        return LocationData(
            TestConstants.LATITUDE,
            TestConstants.LONGITUDE,
            TestConstants.ADMIN_AREA,
            TestConstants.LOCALITY
        )
    }

    private fun getExpectedState(date: LocalDate): HomeState {
        return HomeState(
            isLoading = false,
            date = date,
            latLong = Pair(TestConstants.LATITUDE, TestConstants.LONGITUDE),
            timeZone = -5,
            majorOne = TestConstants.FORMATTED_MAJOR_ONE,
            majorTwo = TestConstants.FORMATTED_MAJOR_TWO,
            minorOne = TestConstants.FORMATTED_MINOR_ONE,
            minorTwo = TestConstants.FORMATTED_MINOR_TWO,
            locationName = TestConstants.MINOCQUA_WI,
            dayRating = Pair(69, 0.6888889f),
            requestLocationState = RequestLocationState.LOCATION_REQUEST_SUCCESSFUL,
            isSolunarResponseSuccessful = true
        )
    }
}
