package com.timserio.home_data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.timserio.home_domain.location.GeocodeLocation
import com.timserio.home_domain.location.LocationData
import com.timserio.home_domain.location.LocationTracker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

@ExperimentalCoroutinesApi
class LocationTrackerImpl @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    private val geocodeLocation: GeocodeLocation,
    private val context: Context
) : LocationTracker {

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): LocationData? {
        val hasAccessFineLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasAccessCoarseLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!hasAccessFineLocation || !hasAccessCoarseLocation || !isGpsEnabled) return null
        return suspendCancellableCoroutine { cont ->
            locationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

                    override fun isCancellationRequested() = false
                }
            )
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        geocodeLocation.geocodeLocation(Pair(location.latitude, location.longitude)) {
                            cont.resume(it)
                        }
                    } ?: run {
                        cont.resume(null)
                    }
                }
                .addOnCanceledListener {
                    cont.cancel()
                }
                .addOnFailureListener {
                    cont.resume(null)
                }
        }
    }
}
