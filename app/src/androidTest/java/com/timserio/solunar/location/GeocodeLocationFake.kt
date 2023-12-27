package com.timserio.solunar.location

import com.timserio.home_domain.location.GeocodeLocation
import com.timserio.home_domain.location.LocationData
import com.timserio.test_utils.TestConstants

class GeocodeLocationFake : GeocodeLocation {
    override fun geocodeLocation(location: Pair<Double, Double>, callback: (LocationData) -> Unit) {
        callback(
            LocationData(
                location.first,
                location.second,
                TestConstants.ADMIN_AREA,
                TestConstants.LOCALITY
            )
        )
    }
}
