package com.timserio.home_domain.use_case

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetDayRatingTest {

    private lateinit var getDayRating: GetDayRating

    @Before
    fun setup() {
        getDayRating = GetDayRating()
    }

    @Test
    fun `Day rating is calculated correctly`() {
        val expected = Pair(40, 0.4f)
        val actual = getDayRating(listOf(30, 30, 60, 0, 0, 20, 10, 0, 0, 40, 30, 0, 0, 10, 40, 20, 0, 0, 0, 40, 20, 10, 0))
        assertEquals(expected, actual)
    }
}
