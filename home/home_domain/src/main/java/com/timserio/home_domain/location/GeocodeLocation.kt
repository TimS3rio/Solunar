package com.timserio.home_domain.location

interface GeocodeLocation {
    fun geocodeLocation(location: Pair<Double, Double>, callback: (LocationData) -> Unit)
}
