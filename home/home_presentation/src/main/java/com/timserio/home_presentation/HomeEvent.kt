package com.timserio.home_presentation

import com.timserio.home_domain.location.LocationData
import com.timserio.home_domain.model.SolunarData
import java.time.LocalDate

sealed class HomeEvent {
    object OnNextDayClick : HomeEvent()
    object OnPreviousDayClick : HomeEvent()
    object OnLocationGranted : HomeEvent()
    object OnLoading : HomeEvent()
    object OnDismissRationale : HomeEvent()
    object OnDismissGoToAppSettings : HomeEvent()
    data class OnPermissionResult(val isGranted: Boolean) : HomeEvent()
    data class OnSetDate(val date: LocalDate) : HomeEvent()
    data class OnCurrentLocationLoaded(val location: LocationData?) : HomeEvent()
    data class OnSolunarTimesLoaded(val data: SolunarData?) : HomeEvent()
    data class OnLocationSelectedFromMap(val latLong: Pair<Double, Double>) : HomeEvent()
    data class OnLocationFromMapGeocoded(val location: LocationData?) : HomeEvent()
}
