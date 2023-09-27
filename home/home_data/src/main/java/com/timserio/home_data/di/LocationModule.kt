package com.timserio.home_data.di

import com.timserio.home_data.location.GeocodeLocationImpl
import com.timserio.home_data.location.LocationTrackerImpl
import com.timserio.home_domain.location.GeocodeLocation
import com.timserio.home_domain.location.LocationTracker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    @Singleton
    abstract fun bindLocationTracker(tracker: LocationTrackerImpl): LocationTracker

    @Binds
    @Singleton
    abstract fun bindGeocodeLocation(tracker: GeocodeLocationImpl): GeocodeLocation
}
