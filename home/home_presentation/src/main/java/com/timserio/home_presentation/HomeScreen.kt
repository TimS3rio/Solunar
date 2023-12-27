package com.timserio.home_presentation

import android.Manifest
import android.content.res.Configuration
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import com.timserio.core.R
import com.timserio.home_presentation.components.FloatingActionButtonState
import com.timserio.home_presentation.components.Identifier
import com.timserio.home_presentation.components.LandscapeContent
import com.timserio.home_presentation.components.MiniFAB
import com.timserio.home_presentation.components.PortraitContent
import com.timserio.home_presentation.components.SolunarFAB

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    permissionResultLauncher: ActivityResultLauncher<String>,
    onSelectLocationClicked: () -> Unit,
    onEvent: (HomeEvent) -> Unit,
    locationFromMap: Pair<Double, Double>?
) {
    val miniFabItems = listOf(
        MiniFAB(
            icon = R.drawable.ic_location,
            label = stringResource(id = R.string.get_current_location),
            id = Identifier.GET_CURRENT_LOCATION
        ),
        MiniFAB(
            icon = R.drawable.ic_place,
            label = stringResource(id = R.string.select_location),
            id = Identifier.SELECT_LOCATION
        )
    )
    var getCurrentLocation by rememberSaveable {
        mutableStateOf(false)
    }
    if (
        getCurrentLocation ||
        state.requestLocationState == RequestLocationState.REQUESTING_PERMISSIONS
    ) {
        permissionResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        getCurrentLocation = false
    }
    Scaffold(floatingActionButton = {
        var fabState by rememberSaveable {
            mutableStateOf(FloatingActionButtonState.COLLAPSED)
        }

        if (
            state.requestLocationState == RequestLocationState.LOCATION_REQUEST_SUCCESSFUL &&
            state.isSolunarResponseSuccessful != false
        ) {
            SolunarFAB(
                fabState = fabState,
                onFabStateChanged = {
                    fabState = it
                },
                items = miniFabItems,
                onSelectLocationClicked = { onSelectLocationClicked() },
                onGetCurrentLocationClicked = { getCurrentLocation = true }
            )
        }
    }) { padding ->
        locationFromMap?.let {
            onEvent(HomeEvent.OnLocationSelectedFromMap(it))
        }

        val gradient = Brush.linearGradient(
            0.0f to MaterialTheme.colorScheme.primary,
            500.0f to MaterialTheme.colorScheme.secondary,
            start = Offset.Zero,
            end = Offset.Infinite
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(gradient)
                .padding(padding)
        )
        val configuration = LocalConfiguration.current
        when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE ->
                LandscapeContent(
                    state = state,
                    onEvent = { onEvent(it) },
                    onSelectLocationClicked = onSelectLocationClicked,
                    permissionResultLauncher = permissionResultLauncher
                )
            else ->
                PortraitContent(
                    state = state,
                    onEvent = { onEvent(it) },
                    onSelectLocationClicked = onSelectLocationClicked,
                    permissionResultLauncher = permissionResultLauncher
                )
        }
    }
}
