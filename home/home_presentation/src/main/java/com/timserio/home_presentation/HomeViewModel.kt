package com.timserio.home_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timserio.core.util.StateReducerFlow
import com.timserio.home_domain.location.GeocodeLocation
import com.timserio.home_domain.location.LocationTracker
import com.timserio.home_domain.model.SolunarData
import com.timserio.home_domain.use_case.HomeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases,
    private val locationTracker: LocationTracker,
    private val geocodeLocation: GeocodeLocation
) : ViewModel() {

    val state = StateReducerFlow(
        initialState = HomeState(),
        reduceState = ::reduceState
    )

    private fun reduceState(
        currentState: HomeState,
        event: HomeEvent
    ): HomeState {
        var result = currentState
        when (event) {
            is HomeEvent.OnPreviousDayClick -> {
                result = currentState.copy(
                    date = currentState.date.minusDays(1)
                )
                getSolunarTimes(result)
            }
            is HomeEvent.OnNextDayClick -> {
                result = currentState.copy(
                    date = currentState.date.plusDays(1)
                )
                getSolunarTimes(result)
            }
            is HomeEvent.OnLocationGranted -> {
                result = currentState.copy(
                    requestLocationState = RequestLocationState.REQUESTING_PERMISSIONS
                )
                getCurrentLocation()
            }
            is HomeEvent.OnDismissRationale -> {
                result = currentState.copy(
                    showPermissionRationale = false
                )
            }
            is HomeEvent.OnDismissGoToAppSettings -> {
                result = currentState.copy(
                    showPermissionRationale = false
                )
            }
            is HomeEvent.OnPermissionResult -> {
                if (!event.isGranted && !currentState.showPermissionRationale) {
                    result = currentState.copy(
                        showPermissionRationale = true
                    )
                }
            }
            is HomeEvent.OnSetDate -> {
                result = currentState.copy(
                    date = event.date
                )
            }
            is HomeEvent.OnLoading -> {
                result = currentState.copy(
                    isLoading = true,
                    requestLocationState = RequestLocationState.LOCATION_REQUEST_SUCCESSFUL
                )
            }
            is HomeEvent.OnCurrentLocationLoaded -> {
                event.location?.let { locationData ->
                    letTwo(locationData.latitude, locationData.longitude) { lat, long ->
                        result = currentState.copy(
                            latLong = Pair(lat, long),
                            locationName = homeUseCases.getLocationName(
                                locationData.locality,
                                locationData.adminArea
                            )
                        )
                        getSolunarTimes(result)
                    } ?: run {
                        result = currentState.copy(requestLocationState = RequestLocationState.LOCATION_REQUEST_FAILED)
                    }
                } ?: run {
                    result = currentState.copy(requestLocationState = RequestLocationState.LOCATION_REQUEST_FAILED)
                }
            }
            is HomeEvent.OnSolunarTimesLoaded -> {
                event.data?.let {
                    if (isSolunarDataEmpty(it)) {
                        result = currentState.copy(isLoading = false, isSolunarResponseSuccessful = false)
                    } else {
                        val formattedTimes = homeUseCases.formatSolunarTimes(it)
                        var dayRating = Pair(0, 0f)
                        it.hourlyRatings?.let { hourlyRatings ->
                            dayRating = homeUseCases.getDayRating(hourlyRatings)
                        }
                        result = currentState.copy(
                            majorOne = formattedTimes.majorOne,
                            majorTwo = formattedTimes.majorTwo,
                            minorOne = formattedTimes.minorOne,
                            minorTwo = formattedTimes.minorTwo,
                            dayRating = dayRating,
                            isLoading = false,
                            isSolunarResponseSuccessful = true
                        )
                    }
                } ?: run {
                    result = currentState.copy(isLoading = false, isSolunarResponseSuccessful = false)
                }
            }
            is HomeEvent.OnLocationFromMapGeocoded -> {
                val location = event.location
                location?.let {
                    letTwo(it.latitude, it.longitude) { lat, long ->
                        result = currentState.copy(
                            latLong = Pair(lat, long),
                            requestLocationState = RequestLocationState.LOCATION_REQUEST_SUCCESSFUL,
                            locationName = homeUseCases.getLocationName(
                                it.locality,
                                it.adminArea
                            )
                        )
                        getSolunarTimes(result)
                    }
                }
            }
            is HomeEvent.OnLocationSelectedFromMap -> {
                geocodeLocation.geocodeLocation(Pair(event.latLong.first, event.latLong.second)) {
                    state.handleEvent(HomeEvent.OnLocationFromMapGeocoded(it))
                }
            }
        }
        return result
    }

    private fun getCurrentLocation() {
        viewModelScope.launch {
            locationTracker.getCurrentLocation()?.let { locationData ->
                state.handleEvent(HomeEvent.OnCurrentLocationLoaded(locationData))
            } ?: run {
                state.handleEvent(HomeEvent.OnCurrentLocationLoaded(null))
            }
        }
    }

    private fun getSolunarTimes(currentState: HomeState) {
        viewModelScope.launch {
            currentState.latLong?.let { latLong ->
                state.handleEvent(HomeEvent.OnLoading)
                homeUseCases.getSolunarTimes(
                    latLong.first.toString(),
                    latLong.second.toString(),
                    homeUseCases.formatDateForRequest(currentState.date),
                    homeUseCases.getTimezoneRegion()
                )
                    .onSuccess {
                        state.handleEvent(HomeEvent.OnSolunarTimesLoaded(it))
                    }
                    .onFailure {
                        state.handleEvent(HomeEvent.OnSolunarTimesLoaded(null))
                    }
            } ?: run {
                state.handleEvent(HomeEvent.OnCurrentLocationLoaded(null))
            }
        }
    }

    private fun isSolunarDataEmpty(solunarData: SolunarData): Boolean {
        return solunarData.majorOneStart == null && solunarData.majorOneEnd == null &&
            solunarData.majorTwoStart == null && solunarData.majorTwoEnd == null &&
            solunarData.minorOneStart == null && solunarData.minorOneEnd == null &&
            solunarData.minorTwoStart == null && solunarData.minorTwoEnd == null
    }

    private fun <T1 : Any, T2 : Any, R : Any> letTwo(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
        return if (p1 != null && p2 != null) block(p1, p2) else null
    }
}
