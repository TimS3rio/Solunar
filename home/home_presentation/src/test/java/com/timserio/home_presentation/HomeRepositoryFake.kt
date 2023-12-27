package com.timserio.home_presentation

import com.timserio.home_domain.model.SolunarData
import com.timserio.home_domain.repository.HomeRepository
import com.timserio.test_utils.TestConstants

class HomeRepositoryFake : HomeRepository {
    var shouldReturnError = false
    var solunarData = SolunarData(
        TestConstants.MAJOR_ONE_START,
        TestConstants.MAJOR_ONE_END,
        TestConstants.MAJOR_TWO_START,
        TestConstants.MAJOR_TWO_END,
        TestConstants.MINOR_ONE_START,
        TestConstants.MINOR_ONE_END,
        TestConstants.MINOR_TWO_START,
        TestConstants.MINOR_TWO_END,
        listOf(
            50, 60, 50, 20, 10, 0, 0, 10,
            20, 20, 20, 30, 0, 60, 70, 60,
            40, 30, 0, 0, 0, 10, 0, 0,
            0, 0, 10, 20, 20, 10, 0, 0
        )
    )

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
