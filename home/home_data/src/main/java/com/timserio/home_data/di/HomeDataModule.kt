package com.timserio.home_data.di

import android.app.Application
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.timserio.home_data.location.GeocodeLocationImpl
import com.timserio.home_data.location.LocationTrackerImpl
import com.timserio.home_data.remote.SolunarApi
import com.timserio.home_data.repository.HomeRepositoryImpl
import com.timserio.home_domain.location.GeocodeLocation
import com.timserio.home_domain.location.LocationTracker
import com.timserio.home_domain.repository.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeDataModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideSolunarApi(client: OkHttpClient): SolunarApi {
        return Retrofit.Builder()
            .baseUrl(SolunarApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideHomeRepository(api: SolunarApi): HomeRepository {
        return HomeRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(app: Application): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(app)
    }

    @Provides
    @Singleton
    fun provideGeocodeLocation(app: Application): GeocodeLocation {
        return GeocodeLocationImpl(app)
    }

    @Provides
    @Singleton
    fun provideLocationTracker(
        locationClient: FusedLocationProviderClient,
        geocodeLocation: GeocodeLocation,
        app: Application
    ): LocationTracker {
        return LocationTrackerImpl(locationClient, geocodeLocation, app)
    }
}
