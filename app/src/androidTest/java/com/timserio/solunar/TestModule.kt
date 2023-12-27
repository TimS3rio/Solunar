package com.timserio.solunar

import com.timserio.home_data.remote.SolunarApi
import com.timserio.home_data.repository.HomeRepositoryImpl
import com.timserio.home_domain.location.GeocodeLocation
import com.timserio.home_domain.location.LocationTracker
import com.timserio.home_domain.repository.HomeRepository
import com.timserio.solunar.location.GeocodeLocationFake
import com.timserio.solunar.location.LocationTrackerFake
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
object TestModule {

    private const val TEST_URL = "http://localhost:8081/"

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    fun provideSolunarApi(client: OkHttpClient): SolunarApi {
        return Retrofit.Builder()
            .baseUrl(TEST_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
            .create()
    }

    @Provides
    fun provideHomeRepository(api: SolunarApi): HomeRepository {
        return HomeRepositoryImpl(api)
    }

    @Provides
    fun provideLocationTrackerFake(): LocationTracker {
        return LocationTrackerFake()
    }

    @Provides
    fun provideGeocodeLocationFake(): GeocodeLocation {
        return GeocodeLocationFake()
    }
}
