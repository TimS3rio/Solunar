package com.timserio.home_domain.location

interface LocationTracker {
    suspend fun getCurrentLocation(): LocationData?
}
