package com.timserio.select_location_presentation

import com.google.android.gms.maps.model.LatLng

data class SelectLocationState(
    val isLocationSelected: Boolean = false,
    val selectedLocation: LatLng? = null
)
