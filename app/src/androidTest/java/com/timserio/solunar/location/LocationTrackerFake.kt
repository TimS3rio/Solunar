package com.timserio.solunar.location

import com.timserio.home_domain.location.LocationData
import com.timserio.home_domain.location.LocationTracker

class LocationTrackerFake : LocationTracker {
    var shouldReturnError = false
    override suspend fun getCurrentLocation(): LocationData? {
        return if (shouldReturnError) {
            null
        } else {
            LocationData(
                45.8711,
                -89.7093,
                "Wisconsin",
                "Minocqua"
            )
        }
    }
}
