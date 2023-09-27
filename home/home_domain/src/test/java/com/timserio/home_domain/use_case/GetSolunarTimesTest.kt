package com.timserio.home_domain.use_case

import com.timserio.home_domain.model.SolunarData
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetSolunarTimesTest {

    companion object {
        private const val LATITUDE = "45.8711"
        private const val LONGITUDE = "-89.7093"
        private const val DATE = "20230912"
        private const val TIMEZONE = -5
        private const val MAJOR_ONE_START = "00:00"
        private const val MAJOR_ONE_END = "02:00"
        private const val MAJOR_TWO_START = "13:15"
        private const val MAJOR_TWO_END = "15:15"
        private const val MINOR_ONE_START = "22:59"
        private const val MINOR_ONE_END = "23:59"
        private const val MINOR_TWO_START = "08:30"
        private const val MINOR_TWO_END = "09:30"
    }

    private lateinit var getSolunarTimes: GetSolunarTimes
    private lateinit var repository: HomeRepositoryFake

    @Before
    fun setup() {
        repository = HomeRepositoryFake()
        repository.solunarData = SolunarData(
            MAJOR_ONE_START,
            MAJOR_ONE_END,
            MAJOR_TWO_START,
            MAJOR_TWO_END,
            MINOR_ONE_START,
            MINOR_ONE_END,
            MINOR_TWO_START,
            MINOR_TWO_END,
            listOf()
        )
        getSolunarTimes = GetSolunarTimes(repository)
    }

    @Test
    fun `Get solunar times success test`() = runBlocking {
        val actual = getSolunarTimes(LATITUDE, LONGITUDE, DATE, TIMEZONE)
        assertEquals(true, actual.isSuccess)
        val expected = SolunarData(
            MAJOR_ONE_START,
            MAJOR_ONE_END,
            MAJOR_TWO_START,
            MAJOR_TWO_END,
            MINOR_ONE_START,
            MINOR_ONE_END,
            MINOR_TWO_START,
            MINOR_TWO_END,
            listOf()
        )
        assertEquals(expected, actual.getOrNull())
    }

    @Test
    fun `Get solunar times failure test`() = runBlocking {
        repository.shouldReturnError = true
        val actual = getSolunarTimes(LATITUDE, LONGITUDE, DATE, TIMEZONE)
        assertEquals(false, actual.isSuccess)
    }
}
