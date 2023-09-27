package com.timserio.home_domain.use_case

import com.timserio.home_domain.model.FormattedSolunarTimes
import com.timserio.home_domain.model.SolunarData
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FormatSolunarTimesTest {

    companion object {
        private const val WRONG_FORMAT = "xx"
        private const val MAJOR_ONE_START = "00:00"
        private const val MAJOR_ONE_END = "02:00"
        private const val FORMATTED_MAJOR_ONE = "12:00 AM - 2:00 AM"
        private const val MAJOR_TWO_START = "13:15"
        private const val MAJOR_TWO_END = "15:15"
        private const val FORMATTED_MAJOR_TWO = "1:15 PM - 3:15 PM"
        private const val MINOR_ONE_START = "22:59"
        private const val MINOR_ONE_END = "23:59"
        private const val FORMATTED_MINOR_ONE = "10:59 PM - 11:59 PM"
        private const val MINOR_TWO_START = "08:30"
        private const val MINOR_TWO_END = "09:30"
        private const val FORMATTED_MINOR_TWO = "8:30 AM - 9:30 AM"
    }

    private lateinit var formatSolunarTimes: FormatSolunarTimes

    @Before
    fun setup() {
        formatSolunarTimes = FormatSolunarTimes()
    }

    @Test
    fun `Solunar times are formatted correctly`() {
        val data = SolunarData(
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
        val expected = FormattedSolunarTimes(
            FORMATTED_MAJOR_ONE,
            FORMATTED_MAJOR_TWO,
            FORMATTED_MINOR_ONE,
            FORMATTED_MINOR_TWO
        )
        assertEquals(expected, formatSolunarTimes(data))
    }

    @Test
    fun `Null major start is formatted correctly`() {
        val data = SolunarData(
            null,
            MAJOR_ONE_END,
            MAJOR_TWO_START,
            MAJOR_TWO_END,
            MINOR_ONE_START,
            MINOR_ONE_END,
            MINOR_TWO_START,
            MINOR_TWO_END,
            listOf()
        )
        val expected = FormattedSolunarTimes(
            "",
            FORMATTED_MAJOR_TWO,
            FORMATTED_MINOR_ONE,
            FORMATTED_MINOR_TWO
        )
        assertEquals(expected, formatSolunarTimes(data))
    }

    @Test
    fun `Null major end is formatted correctly`() {
        val data = SolunarData(
            MAJOR_ONE_START,
            null,
            MAJOR_TWO_START,
            MAJOR_TWO_END,
            MINOR_ONE_START,
            MINOR_ONE_END,
            MINOR_TWO_START,
            MINOR_TWO_END,
            listOf()
        )
        val expected = FormattedSolunarTimes(
            "",
            FORMATTED_MAJOR_TWO,
            FORMATTED_MINOR_ONE,
            FORMATTED_MINOR_TWO
        )
        assertEquals(expected, formatSolunarTimes(data))
    }

    @Test
    fun `Null major start and end is formatted correctly`() {
        val data = SolunarData(
            null,
            null,
            MAJOR_TWO_START,
            MAJOR_TWO_END,
            MINOR_ONE_START,
            MINOR_ONE_END,
            MINOR_TWO_START,
            MINOR_TWO_END,
            listOf()
        )
        val expected = FormattedSolunarTimes(
            "",
            FORMATTED_MAJOR_TWO,
            FORMATTED_MINOR_ONE,
            FORMATTED_MINOR_TWO
        )
        assertEquals(expected, formatSolunarTimes(data))
    }

    @Test
    fun `Empty string time is formatted correctly`() {
        val data = SolunarData(
            "",
            "",
            MAJOR_TWO_START,
            MAJOR_TWO_END,
            MINOR_ONE_START,
            MINOR_ONE_END,
            MINOR_TWO_START,
            MINOR_TWO_END,
            listOf()
        )
        val expected = FormattedSolunarTimes(
            "",
            FORMATTED_MAJOR_TWO,
            FORMATTED_MINOR_ONE,
            FORMATTED_MINOR_TWO
        )
        assertEquals(expected, formatSolunarTimes(data))
    }

    @Test
    fun `Wrong format is formatted correctly`() {
        val data = SolunarData(
            WRONG_FORMAT,
            WRONG_FORMAT,
            MAJOR_TWO_START,
            MAJOR_TWO_END,
            MINOR_ONE_START,
            MINOR_ONE_END,
            MINOR_TWO_START,
            MINOR_TWO_END,
            listOf()
        )
        val expected = FormattedSolunarTimes(
            "",
            FORMATTED_MAJOR_TWO,
            FORMATTED_MINOR_ONE,
            FORMATTED_MINOR_TWO
        )
        assertEquals(expected, formatSolunarTimes(data))
    }
}
