package com.timserio.home_presentation

import com.timserio.home_domain.location.LocationData
import com.timserio.home_domain.model.SolunarData

sealed class HomeEvent {
    object OnNextDayClick : HomeEvent()
    object OnPreviousDayClick : HomeEvent()
    object OnLocationGranted : HomeEvent()
    object OnLocationDenied : HomeEvent()
    object OnLoading : HomeEvent()
    data class OnCurrentLocationLoaded(val location: LocationData?) : HomeEvent()
    data class OnSolunarTimesLoaded(val data: SolunarData?) : HomeEvent()
}
