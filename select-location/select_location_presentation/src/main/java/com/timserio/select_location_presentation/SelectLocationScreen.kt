package com.timserio.select_location_presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.timserio.core.R
import com.timserio.core_ui.ui.theme.DeepBlue
import com.timserio.select_location_presentation.components.LocationNotSelectedSheet
import com.timserio.select_location_presentation.components.LocationSelectedSheet

private const val DEFAULT_LATITUDE = 45.8711
private const val DEFAULT_LONGITUDE = -89.7093

@Composable
fun SelectLocationScreen(
    latLong: Pair<Double, Double>?,
    state: SelectLocationState,
    onEvent: (SelectLocationEvent) -> Unit,
    onLocationSelected: () -> Unit
) {
    val uiSettings = remember {
        MapUiSettings(zoomControlsEnabled = false)
    }
    val location = latLong?.let {
        LatLng(it.first, it.second)
    } ?: run {
        LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
    }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 15f)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            GoogleMap(
                modifier = Modifier.fillMaxWidth(),
                uiSettings = uiSettings,
                cameraPositionState = cameraPositionState,
                contentDescription = stringResource(id = R.string.select_location_on_map),
                onMapLongClick = {
                    onEvent(SelectLocationEvent.OnMapLongClick(it))
                }
            ) {
                state.selectedLocation?.let { latLong ->
                    Marker(
                        position = LatLng(latLong.latitude, latLong.longitude),
                        title = stringResource(id = R.string.gps_coordinates, latLong.latitude.toString(), latLong.longitude.toString()),
                        snippet = stringResource(id = R.string.long_click_to_cancel),
                        onInfoWindowLongClick = {
                            onEvent(SelectLocationEvent.OnInfoWindowLongClick)
                        },
                        onClick = {
                            it.showInfoWindow()
                            true
                        },
                        icon = BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_CYAN
                        )
                    )
                }
            }
        }
        Card(
            colors = CardDefaults.cardColors(containerColor = DeepBlue),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box {
                LocationNotSelectedSheet(alpha = if (state.isLocationSelected) 0f else 1f)
                LocationSelectedSheet(
                    alpha = if (state.isLocationSelected) 1f else 0f,
                    onEvent = { onEvent(it) },
                    onLocationSelected = { onLocationSelected() }
                )
            }
        }
    }
}
