package com.timserio.home_presentation.components

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.DisposableEffectResult
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.timserio.core.R
import kotlinx.coroutines.launch

@ExperimentalPermissionsApi
@Composable
fun RequestLocationPermission(
    cancelCallback: () -> Unit,
    snackbarHostState: SnackbarHostState,
    callback: () -> Unit
) {
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    HandleRequest(
        permissionState = permissionState,
        deniedContent = { shouldShowRationale ->
            PermissionDeniedContent(
                rationaleMessage = stringResource(id = R.string.location_permission_rationale),
                shouldShowRationale = shouldShowRationale,
                onRequestPermission = { permissionState.launchPermissionRequest() },
                onCancelClick = { cancelCallback() }
            )
        },
        content = {
            LocationComponent(snackbarHostState = snackbarHostState) {
                callback.invoke()
            }
        }
    )
}

@ExperimentalPermissionsApi
@Composable
private fun HandleRequest(
    permissionState: PermissionState,
    deniedContent: @Composable (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    when (permissionState.status) {
        is PermissionStatus.Granted -> {
            content()
        }
        is PermissionStatus.Denied -> {
            deniedContent(permissionState.status.shouldShowRationale)
        }
    }
}

@Composable
private fun Content(showButton: Boolean = true, onClick: () -> Unit, onCancelClick: () -> Unit) {
    if (showButton) {
        val enableLocation = remember { mutableStateOf(true) }
        if (enableLocation.value) {
            LocationDialog(
                title = stringResource(id = R.string.turn_on_location),
                description = stringResource(id = R.string.turn_on_location_desc),
                enableLocation,
                onClick,
                onCancelClick
            )
        }
    }
}

@ExperimentalPermissionsApi
@Composable
private fun PermissionDeniedContent(
    rationaleMessage: String,
    shouldShowRationale: Boolean,
    onRequestPermission: () -> Unit,
    onCancelClick: () -> Unit
) {
    if (shouldShowRationale) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    text = stringResource(id = R.string.location_permission_request),
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            text = {
                Text(rationaleMessage)
            },
            confirmButton = {
                Button(onClick = onRequestPermission) {
                    Text(stringResource(id = R.string.grant_permission))
                }
            }
        )
    } else {
        Content(onClick = onRequestPermission, onCancelClick = onCancelClick)
    }
}

private object SimulatedDisposableEffectResult : DisposableEffectResult {
    override fun dispose() {}
}

@OptIn(ExperimentalPermissionsApi::class)
sealed interface LocationPermissionsState {
    @Composable
    operator fun invoke(): MultiplePermissionsState

    object CoarseAndFine : LocationPermissionsState {
        @Composable
        override fun invoke(): MultiplePermissionsState {
            return rememberMultiplePermissionsState(
                permissions = listOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                )
            )
        }
    }
}

private suspend fun requestToEnableGPS(
    context: Context,
    snackbarHostState: SnackbarHostState,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    val canNavigateToGPSSettings = try {
        launcher.launch(intent)
        true
    } catch (e: ActivityNotFoundException) {
        false
    }

    val result = snackbarHostState.showSnackbar(
        message = context.resources.getString(R.string.gps_disabled),
        actionLabel = if (!canNavigateToGPSSettings) {
            null
        } else {
            context.resources.getString(R.string.enable)
        },
        withDismissAction = true,
        duration = SnackbarDuration.Indefinite,
    )

    when (result) {
        SnackbarResult.Dismissed -> {}

        SnackbarResult.ActionPerformed -> {
            if (canNavigateToGPSSettings) launcher.launch(intent)
        }
    }
}

private fun isGPSEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(
        Context.LOCATION_SERVICE
    ) as LocationManager

    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}

@Composable
@ExperimentalPermissionsApi
fun locationPermissionsGranted(): Boolean {
    return rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    ).allPermissionsGranted
}

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("MissingPermission")
@Composable
private fun LocationComponent(
    snackbarHostState: SnackbarHostState,
    permissionsState: LocationPermissionsState = LocationPermissionsState.CoarseAndFine,
    onGpsEnabled: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val permissions = permissionsState()

    var isGPSEnabled by remember {
        mutableStateOf(isGPSEnabled(context))
    }

    LaunchedEffect(true) {
        isGPSEnabled = isGPSEnabled(context)
    }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult(), onResult = {
        onGpsEnabled()
    })

    DisposableEffect(
        isGPSEnabled,
        permissions.shouldShowRationale,
        permissions.allPermissionsGranted,
    ) {
        if (!permissions.allPermissionsGranted || permissions.shouldShowRationale) {
            scope.launch {
                val result = snackbarHostState.showSnackbar(
                    context.resources.getString(R.string.missing_permissions),
                    context.resources.getString(R.string.grant),
                    withDismissAction = true,
                    duration = SnackbarDuration.Indefinite,
                )

                when (result) {
                    SnackbarResult.Dismissed -> { }

                    SnackbarResult.ActionPerformed -> {
                        permissions.launchMultiplePermissionRequest()
                    }
                }
            }
            return@DisposableEffect SimulatedDisposableEffectResult
        }

        if (!isGPSEnabled) {
            scope.launch {
                requestToEnableGPS(
                    context = context,
                    snackbarHostState = snackbarHostState,
                    launcher
                )
            }
            return@DisposableEffect SimulatedDisposableEffectResult
        }

        onGpsEnabled()

        return@DisposableEffect SimulatedDisposableEffectResult
    }
}
