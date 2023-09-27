package com.timserio.home_data.repository

import com.timserio.home_data.remote.SolunarApi
import com.timserio.home_data.remote.malformedSolunarResponse
import com.timserio.home_data.remote.validSolunarResponse
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class HomeRepositoryImplTest {

    companion object {
        private const val LATITUDE = "45.8711"
        private const val LONGITUDE = "-89.7093"
        private const val DATE = "20230912"
        private const val TIMEZONE = -5
    }

    private lateinit var homeRepositoryImpl: HomeRepositoryImpl
    private lateinit var mockWebServer: MockWebServer
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var api: SolunarApi

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        okHttpClient = OkHttpClient.Builder()
            .readTimeout(1, TimeUnit.SECONDS)
            .writeTimeout(1, TimeUnit.SECONDS)
            .connectTimeout(1, TimeUnit.SECONDS)
            .build()
        api = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(mockWebServer.url("/"))
            .build()
            .create(SolunarApi::class.java)
        homeRepositoryImpl = HomeRepositoryImpl(api)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Get solunar times, valid response, returns solunar times`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(validSolunarResponse)
        )
        val result = homeRepositoryImpl.getSolunarTimes(LATITUDE, LONGITUDE, DATE, TIMEZONE)
        assertEquals(true, result.isSuccess)
    }

    @Test
    fun `Get solunar times, error response, returns failure`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(403)
                .setBody(validSolunarResponse)
        )
        val result = homeRepositoryImpl.getSolunarTimes(LATITUDE, LONGITUDE, DATE, TIMEZONE)
        assertEquals(true, result.isFailure)
    }

    @Test
    fun `Get solunar times, malformed response, returns failure`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setBody(malformedSolunarResponse)
        )
        val result = homeRepositoryImpl.getSolunarTimes(LATITUDE, LONGITUDE, DATE, TIMEZONE)
        assertEquals(true, result.isFailure)
    }
}
