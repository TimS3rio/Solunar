package com.timserio.solunar

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.timserio.home_presentation.HomeEvent
import com.timserio.home_presentation.HomeViewModel
import com.timserio.home_presentation.components.PermissionDialog
import com.timserio.solunar.navigation.SolunarNavHost
import com.timserio.solunar.ui.theme.SolunarTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SolunarTheme {
                val context = LocalContext.current
                val homeViewModel: HomeViewModel = hiltViewModel()
                val gpsServiceLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult(),
                    onResult = {
                        homeViewModel.onEvent(HomeEvent.OnLocationGranted)
                    }
                )
                val appSettingsLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult(),
                    onResult = {
                        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            if (isGPSEnabled(context)) {
                                homeViewModel.onEvent(HomeEvent.OnLocationGranted)
                            } else {
                                requestToEnableGPS(gpsServiceLauncher)
                            }
                        }
                    }
                )
                val permissionResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        if (isGranted) {
                            if (isGPSEnabled(context)) {
                                homeViewModel.onEvent(HomeEvent.OnLocationGranted)
                            } else {
                                requestToEnableGPS(gpsServiceLauncher)
                            }
                        }
                        homeViewModel.onEvent(HomeEvent.OnPermissionResult(isGranted))
                    }
                )
                val state = homeViewModel.state.collectAsState().value
                if (state.showPermissionRationale) {
                    PermissionDialog(
                        isPermanentlyDeclined = !ActivityCompat.shouldShowRequestPermissionRationale(
                            this@MainActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ),
                        onDismiss = { homeViewModel.onEvent(HomeEvent.OnDismissRationale) },
                        onOkClick = {
                            homeViewModel.onEvent(HomeEvent.OnDismissRationale)
                            permissionResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        },
                        onGoToAppSettingsClick = {
                            homeViewModel.onEvent(HomeEvent.OnDismissGoToAppSettings)
                            openAppSettings(appSettingsLauncher)
                        }
                    )
                }
                val navController = rememberNavController()
                SolunarNavHost(
                    navController = navController,
                    homeViewModel = homeViewModel,
                    permissionResultLauncher = permissionResultLauncher
                )
            }
        }
    }

    private fun HomeViewModel.onEvent(event: HomeEvent) {
        state.handleEvent(event)
    }

    private fun Activity.openAppSettings(
        launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
    ) {
        launcher.launch(
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", packageName, null)
            )
        )
    }

    private fun requestToEnableGPS(
        launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
    ) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        try {
            launcher.launch(intent)
        } catch (e: ActivityNotFoundException) {
            Log.i("requestToEnableGPS: ", "e: ${e.message}")
        }
    }

    private fun isGPSEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}
