package com.timserio.home_domain.use_case

import com.timserio.home_domain.model.SolunarData
import com.timserio.home_domain.repository.HomeRepository

class GetSolunarTimes(private val repository: HomeRepository) {

    suspend operator fun invoke(
        latitude: String,
        longitude: String,
        date: String,
        timeZone: Int
    ): Result<SolunarData> {
        return repository.getSolunarTimes(latitude, longitude, date, timeZone)
    }
}
