package com.timserio.select_location_presentation

import androidx.lifecycle.ViewModel
import com.timserio.core.util.StateReducerFlow

class SelectLocationViewModel : ViewModel() {

    val state = StateReducerFlow(
        initialState = SelectLocationState(),
        reduceState = ::reduceState
    )

    private fun reduceState(
        currentState: SelectLocationState,
        event: SelectLocationEvent
    ): SelectLocationState {
        var result = currentState
        when (event) {
            is SelectLocationEvent.OnMapLongClick -> {
                result = currentState.copy(
                    isLocationSelected = true,
                    selectedLocation = event.latLng
                )
            }
            is SelectLocationEvent.OnSelectBtnClick -> {
                event.callback()
            }
            is SelectLocationEvent.OnInfoWindowLongClick, is SelectLocationEvent.OnCancelClick -> {
                result = currentState.copy(
                    isLocationSelected = false,
                    selectedLocation = null
                )
            }
        }
        return result
    }
}
