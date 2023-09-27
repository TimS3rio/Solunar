package com.timserio.home_domain.use_case

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class FormatDateForRequestTest {

    companion object {
        private const val FORMATTED_DATE = "20230831"
    }

    private lateinit var formatDateForRequest: FormatDateForRequest

    @Before
    fun setup() {
        formatDateForRequest = FormatDateForRequest()
    }

    @Test
    fun `August 31, 2023 is formatted correctly`() {
        val date = LocalDate.of(2023, 8, 31)
        assertEquals(FORMATTED_DATE, formatDateForRequest(date))
    }
}
