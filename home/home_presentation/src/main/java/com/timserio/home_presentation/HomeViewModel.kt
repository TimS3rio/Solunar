package com.timserio.home_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timserio.core.util.StateReducerFlow
import com.timserio.home_domain.location.LocationTracker
import com.timserio.home_domain.use_case.HomeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases,
    private val locationTracker: LocationTracker
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
            is HomeEvent.OnLocationGranted -> getCurrentLocation()
            is HomeEvent.OnLocationDenied -> {
                result = currentState.copy(isLocationRequestSuccessful = false)
            }
            is HomeEvent.OnLoading -> result = currentState.copy(isLoading = true, isLocationRequestSuccessful = true)
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
                        result = currentState.copy(isLocationRequestSuccessful = false)
                    }
                } ?: run {
                    result = currentState.copy(isLocationRequestSuccessful = false)
                }
            }
            is HomeEvent.OnSolunarTimesLoaded -> {
                event.data?.let {
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
                } ?: run {
                    result = currentState.copy(isLoading = false, isSolunarResponseSuccessful = false)
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

    private fun <T1 : Any, T2 : Any, R : Any> letTwo(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
        return if (p1 != null && p2 != null) block(p1, p2) else null
    }
}
