package com.timserio.solunar.location

import com.timserio.home_domain.location.LocationData
import com.timserio.home_domain.location.LocationTracker
import com.timserio.test_utils.TestConstants

class LocationTrackerFake : LocationTracker {
    var shouldReturnError = false
    override suspend fun getCurrentLocation(): LocationData? {
        return if (shouldReturnError) {
            null
        } else {
            LocationData(
                TestConstants.LATITUDE,
                TestConstants.LONGITUDE,
                TestConstants.ADMIN_AREA,
                TestConstants.LOCALITY
            )
        }
    }
}
