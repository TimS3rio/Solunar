package com.timserio.home_presentation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.timserio.home_presentation.components.FloatingActionButtonState
import com.timserio.home_presentation.components.LandscapeContent
import com.timserio.home_presentation.components.MiniFAB
import com.timserio.home_presentation.components.PortraitContent
import com.timserio.home_presentation.components.RequestLocationPermission
import com.timserio.home_presentation.components.SolunarFAB
import com.timserio.core.R
import com.timserio.home_presentation.components.Identifier

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalPermissionsApi
@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit
) {
    val miniFabItems = listOf(
        MiniFAB(
            icon = R.drawable.ic_location,
            label = stringResource(id = R.string.get_current_location),
            id = Identifier.GET_CURRENT_LOCATION
        )
    )
    Scaffold(floatingActionButton = {
        var fabState by rememberSaveable {
            mutableStateOf(FloatingActionButtonState.COLLAPSED)
        }

        var getCurrentLocation by rememberSaveable {
            mutableStateOf(false)
        }

        SolunarFAB(
            fabState = fabState,
            onFabStateChanged = {
                fabState = it
            },
            items = miniFabItems,
            onGetCurrentLocationClicked = { getCurrentLocation = true }
        )
        if (getCurrentLocation) {
            CheckLocationPermission(onEvent = onEvent)
            getCurrentLocation = false
        }
    }) { padding ->
        if (state.isLocationRequestSuccessful != true) {
            CheckLocationPermission(onEvent = onEvent)
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
                LandscapeContent(state = state, onEvent = { onEvent(it) })
            else ->
                PortraitContent(state = state, onEvent = { onEvent(it) })
        }
    }
}

@ExperimentalPermissionsApi
@Composable
private fun CheckLocationPermission(onEvent: (HomeEvent) -> Unit) {
    RequestLocationPermission(
        cancelCallback = { onEvent(HomeEvent.OnLocationDenied) },
        snackbarHostState = SnackbarHostState()
    ) {
        onEvent(HomeEvent.OnLocationGranted)
    }
}
