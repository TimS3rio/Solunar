package com.timserio.solunar

import android.Manifest
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.timserio.core.R
import com.timserio.home_data.di.HomeDataModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@UninstallModules(HomeDataModule::class)
class SolunarEdgeCasesTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule(MainActivity::class.java)

    @get:Rule(order = 2)
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION)

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8081)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getSolunarTimesEmptyTest() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(200).setBody(solunarEmptyResponse)
            }
        }

        // WHEN: The user gets solunar times for their current location and an empty response is returned
        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.get_current_location))
            .performClick()

        advanceClockForAnimations()

        // THEN: The error views are shown
        assertThatErrorViewsAreDisplayed()
    }

    @Test
    fun getSolunarTimesInvalidJsonTest() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(200).setBody(solunarInvalidJsonResponse)
            }
        }

        // WHEN: The user gets solunar times for their current location and invalid JSON is returned
        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.get_current_location))
            .performClick()

        advanceClockForAnimations()

        // THEN: The error views are shown
        assertThatErrorViewsAreDisplayed()
    }

    @Test
    fun getSolunarTimesFailureTest() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(500).setBody(solunarEmptyResponse)
            }
        }

        // WHEN: The user gets solunar times for their current location and an error response is returned
        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.get_current_location))
            .performClick()

        advanceClockForAnimations()

        // THEN: The error views are shown
        assertThatErrorViewsAreDisplayed()
    }

    private fun advanceClockForAnimations() {
        composeRule.mainClock.autoAdvance = false
        composeRule.mainClock.advanceTimeBy(1500)
        composeRule.mainClock.autoAdvance = true
    }

    private fun assertThatErrorViewsAreDisplayed() {
        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.error_msg))
            .assertExists()

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.select_a_location))
            .assertExists()

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.get_current_location))
            .assertExists()

        composeRule
            .onNodeWithContentDescription(UITestingUtil.getResourceString(R.string.edit_location))
            .assertDoesNotExist()
    }
}
