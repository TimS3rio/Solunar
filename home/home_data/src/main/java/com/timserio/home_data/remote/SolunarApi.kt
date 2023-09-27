package com.timserio.home_data.remote

import com.timserio.home_data.remote.dto.SolunarResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface SolunarApi {
    companion object {
        const val BASE_URL = "https://api.solunar.org/"
    }

    @GET("solunar/{latitude},{longitude},{date},{timeZone}")
    suspend fun getSolunarTimes(
        @Path("latitude") latitude: String,
        @Path("longitude") longitude: String,
        @Path("date") date: String,
        @Path("timeZone") timeZone: Int
    ): SolunarResponse
}
