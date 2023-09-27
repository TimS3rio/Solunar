package com.timserio.home_domain.repository

import com.timserio.home_domain.model.SolunarData

interface HomeRepository {
    suspend fun getSolunarTimes(
        latitude: String,
        longitude: String,
        date: String,
        timeZone: Int
    ): Result<SolunarData>
}
