package com.timserio.select_location_presentation

import com.google.android.gms.maps.model.LatLng

sealed class SelectLocationEvent {
    object OnInfoWindowLongClick : SelectLocationEvent()
    object OnCancelClick : SelectLocationEvent()
    data class OnSelectBtnClick(val callback: () -> Unit) : SelectLocationEvent()
    data class OnMapLongClick(val latLng: LatLng) : SelectLocationEvent()
}
