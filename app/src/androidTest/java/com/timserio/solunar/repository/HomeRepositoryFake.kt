package com.timserio.solunar.repository

import com.timserio.home_domain.model.SolunarData
import com.timserio.home_domain.repository.HomeRepository

class HomeRepositoryFake : HomeRepository {
    var shouldReturnError = false
    var solunarData = SolunarData()

    override suspend fun getSolunarTimes(
        latitude: String,
        longitude: String,
        date: String,
        timeZone: Int
    ): Result<SolunarData> {
        return if (shouldReturnError) {
            Result.failure(Throwable())
        } else {
            Result.success(solunarData)
        }
    }
}
