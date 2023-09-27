package com.timserio.home_domain.use_case

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetLocationNameTest {

    companion object {
        private const val CITY = "Minocqua"
        private const val STATE = "Wisconsin"
        private const val EXPECTED_LOCATION_NAME = "Minocqua, WI"
    }

    private lateinit var getLocationName: GetLocationName

    @Before
    fun setup() {
        getLocationName = GetLocationName()
    }

    @Test
    fun `Null address returns correct name`() {
        assertEquals("", getLocationName(null, null))
    }

    @Test
    fun `Empty city and state returns correct name`() {
        assertEquals("", getLocationName("", ""))
    }

    @Test
    fun `Empty state returns correct name`() {
        assertEquals(CITY, getLocationName(CITY, ""))
    }

    @Test
    fun `Empty city returns correct name`() {
        assertEquals(STATE, getLocationName("", STATE))
    }

    @Test
    fun `Address returns correct name`() {
        assertEquals(EXPECTED_LOCATION_NAME, getLocationName(CITY, STATE))
    }
}
