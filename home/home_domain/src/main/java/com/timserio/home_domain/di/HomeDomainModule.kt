package com.timserio.home_domain.di

import com.timserio.home_domain.repository.HomeRepository
import com.timserio.home_domain.use_case.FormatDateForRequest
import com.timserio.home_domain.use_case.FormatSolunarTimes
import com.timserio.home_domain.use_case.GetDayRating
import com.timserio.home_domain.use_case.GetLocationName
import com.timserio.home_domain.use_case.GetSolunarTimes
import com.timserio.home_domain.use_case.GetTimezoneRegion
import com.timserio.home_domain.use_case.HomeUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object HomeDomainModule {
    @ViewModelScoped
    @Provides
    fun provideHomeUseCases(
        repository: HomeRepository
    ): HomeUseCases {
        return HomeUseCases(
            FormatDateForRequest(),
            FormatSolunarTimes(),
            GetSolunarTimes(repository),
            GetTimezoneRegion(),
            GetLocationName(),
            GetDayRating()
        )
    }
}
