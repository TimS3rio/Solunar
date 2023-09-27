package com.timserio.home_domain.use_case

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.TimeZone

class GetTimezoneRegionTest {

    private lateinit var getTimezoneRegion: GetTimezoneRegion

    @Before
    fun setup() {
        getTimezoneRegion = GetTimezoneRegion()
    }

    @Test
    fun `Current timezone returns correct region`() {
        val timeZone = TimeZone.getDefault()
        val expected = if (timeZone.useDaylightTime()) {
            (timeZone.rawOffset + timeZone.dstSavings) / 3600000
        } else {
            timeZone.rawOffset / 3600000
        }
        assertEquals(expected, getTimezoneRegion())
    }
}
