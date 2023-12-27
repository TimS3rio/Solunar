package com.timserio.home_presentation

import com.timserio.home_domain.location.LocationData
import com.timserio.home_domain.location.LocationTracker
import com.timserio.test_utils.TestConstants

class LocationTrackerFake : LocationTracker {
    override suspend fun getCurrentLocation(): LocationData? {
        return LocationData(
            TestConstants.LATITUDE,
            TestConstants.LONGITUDE,
            TestConstants.ADMIN_AREA,
            TestConstants.LOCALITY
        )
    }
}
