package com.timserio.home_data.repository

import com.timserio.home_data.mapper.toSolunarData
import com.timserio.home_data.remote.SolunarApi
import com.timserio.home_domain.model.SolunarData
import com.timserio.home_domain.repository.HomeRepository

class HomeRepositoryImpl(private val api: SolunarApi) : HomeRepository {
    override suspend fun getSolunarTimes(
        latitude: String,
        longitude: String,
        date: String,
        timeZone: Int
    ): Result<SolunarData> {
        return try {
            val solunarResponse = api.getSolunarTimes(latitude, longitude, date, timeZone)
            Result.success(solunarResponse.toSolunarData())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
