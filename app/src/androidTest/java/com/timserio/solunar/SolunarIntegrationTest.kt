package com.timserio.solunar

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.timserio.core.R
import com.timserio.home_data.di.HomeDataModule
import com.timserio.home_data.location.GeocodeLocationImpl
import com.timserio.home_data.location.LocationTrackerImpl
import com.timserio.home_data.remote.SolunarApi
import com.timserio.home_data.repository.HomeRepositoryImpl
import com.timserio.home_domain.location.GeocodeLocation
import com.timserio.home_domain.location.LocationTracker
import com.timserio.home_domain.repository.HomeRepository
import com.timserio.test_utils.TestConstants
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@UninstallModules(HomeDataModule::class, TestModule::class)
class SolunarIntegrationTest {

    companion object {
        private const val TIMEOUT = 4500L
        private const val WHILE_USING_THE_APP = "While using the app"
        private const val DONT_ALLOW_PREFIX = "Don"
        private const val MOUNTAIN_VIEW_CA = "Mountain View, CA"
        private const val TEST_URL = "http://localhost:8081/"
    }

    private val instrumentation = InstrumentationRegistry.getInstrumentation()

    @BindValue
    val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .build()

    @BindValue
    val api: SolunarApi = Retrofit.Builder()
        .baseUrl(TEST_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create()

    @BindValue
    val repository: HomeRepository = HomeRepositoryImpl(api)

    @BindValue
    val locationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(instrumentation.targetContext)

    @BindValue
    val geocodeLocation: GeocodeLocation = GeocodeLocationImpl(instrumentation.targetContext)

    @ExperimentalCoroutinesApi
    @BindValue
    val locationTracker: LocationTracker = LocationTrackerImpl(locationClient, geocodeLocation, instrumentation.targetContext)

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)
    private lateinit var device: UiDevice

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
    fun testGetCurrentLocation() {
        getSolunarTimesByCurrentLocation()
        val locationNameContentDesc = UITestingUtil.getResourceString(R.string.location_name)
        device.wait(Until.hasObject(By.desc(locationNameContentDesc)), TIMEOUT)
        val locationName = device.findObject(By.desc(locationNameContentDesc))
        Assert.assertEquals(MOUNTAIN_VIEW_CA, locationName.text)
    }

    @Test
    fun testDenyLocationPermission() {
        launchApp()

        val getCurrentLocation = UITestingUtil.getResourceString(R.string.get_current_location)
        device.findObject(By.text(getCurrentLocation)).click()
        device.wait(Until.hasObject(By.textStartsWith(DONT_ALLOW_PREFIX)), TIMEOUT)
        device.findObject(By.textStartsWith(DONT_ALLOW_PREFIX)).click()
        val ok = UITestingUtil.getResourceString(R.string.ok)
        device.wait(Until.hasObject(By.text(ok)), TIMEOUT)
        device.findObject(By.text(ok)).click()
        device.wait(Until.hasObject(By.textStartsWith(DONT_ALLOW_PREFIX)), TIMEOUT)
        device.findObject(By.textStartsWith(DONT_ALLOW_PREFIX)).click()
        val grantPermission = UITestingUtil.getResourceString(R.string.grant_permission)
        device.wait(Until.hasObject(By.text(grantPermission)), TIMEOUT)
        device.findObject(By.text(grantPermission)).click()
        device.pressBack()
        device.findObject(By.text(getCurrentLocation)).click()
        val permissionRequired = UITestingUtil.getResourceString(R.string.permission_required)
        val permissionRationale = UITestingUtil.getResourceString(R.string.location_permission_rationale)
        device.wait(Until.hasObject(By.desc(permissionRequired)), TIMEOUT)
        device.wait(Until.hasObject(By.desc(permissionRationale)), TIMEOUT)
        device.wait(Until.hasObject(By.desc(grantPermission)), TIMEOUT)

        val titleText = device.findObject(By.desc(permissionRequired))
        Assert.assertEquals(permissionRequired, titleText.text)
        val messageText = device.findObject(By.desc(permissionRationale))
        Assert.assertEquals(permissionRationale, messageText.text)
        val grantPermissionButton = device.findObject(By.desc(grantPermission))
        Assert.assertEquals(grantPermission, grantPermissionButton.text)
    }

    @Test
    fun testSelectLocationFromMap() {
        launchApp()

        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(200).setBody(solunarSuccessResponse)
            }
        }
        device.findObject(
            By.text(
                UITestingUtil.getResourceString(R.string.select_a_location)
            )
        ).click()

        val selectLocationOnMap = UITestingUtil.getResourceString(R.string.select_location_on_map)
        device.wait(Until.hasObject(By.desc(selectLocationOnMap)), TIMEOUT)
        device.findObject(By.desc(selectLocationOnMap)).longClick()
        val cancel = UITestingUtil.getResourceString(R.string.cancel)
        device.wait(Until.hasObject(By.desc(cancel)), TIMEOUT)
        device.findObject(By.desc(cancel)).click()
        device.findObject(By.desc(selectLocationOnMap)).longClick()
        val selectLocation = UITestingUtil.getResourceString(R.string.select_location)
        device.wait(Until.hasObject(By.desc(selectLocation)), TIMEOUT)
        device.findObject(By.desc(selectLocation)).click()

        val locationNameContentDesc = UITestingUtil.getResourceString(R.string.location_name)
        device.wait(Until.hasObject(By.desc(locationNameContentDesc)), TIMEOUT)
        val locationName = device.findObject(By.desc(locationNameContentDesc))
        Assert.assertEquals(TestConstants.MINOCQUA_WI, locationName.text)
    }

    @Test
    fun testFABSelectLocationFromMap() {
        getSolunarTimesByCurrentLocation()

        val locationNameContentDesc = UITestingUtil.getResourceString(R.string.location_name)
        device.wait(Until.hasObject(By.desc(locationNameContentDesc)), TIMEOUT)
        device.findObject(By.desc(UITestingUtil.getResourceString(R.string.edit_location))).click()
        val selectLocation = UITestingUtil.getResourceString(R.string.select_location)
        device.wait(Until.hasObject(By.desc(selectLocation)), TIMEOUT)
        device.findObject(By.desc(selectLocation)).click()
        val selectLocationOnMap = UITestingUtil.getResourceString(R.string.select_location_on_map)
        device.wait(Until.hasObject(By.desc(selectLocationOnMap)), TIMEOUT)
        device.findObject(By.desc(selectLocationOnMap)).longClick()
        device.wait(Until.hasObject(By.desc(selectLocation)), TIMEOUT)
        device.findObject(By.desc(selectLocation)).click()
        device.wait(Until.hasObject(By.desc(locationNameContentDesc)), TIMEOUT)
        val locationName = device.findObject(By.desc(locationNameContentDesc))
        Assert.assertEquals(MOUNTAIN_VIEW_CA, locationName.text)
    }

    @Test
    fun testFABGetCurrentLocation() {
        getSolunarTimesByCurrentLocation()

        val locationNameContentDesc = UITestingUtil.getResourceString(R.string.location_name)
        device.wait(Until.hasObject(By.desc(locationNameContentDesc)), TIMEOUT)
        device.findObject(By.desc(UITestingUtil.getResourceString(R.string.edit_location))).click()
        val getCurrentLocation = UITestingUtil.getResourceString(R.string.get_current_location)
        device.wait(Until.hasObject(By.desc(getCurrentLocation)), TIMEOUT)
        device.findObject(By.desc(getCurrentLocation)).click()
        device.wait(Until.hasObject(By.desc(UITestingUtil.getResourceString(R.string.loading))), TIMEOUT)
        device.wait(Until.hasObject(By.desc(locationNameContentDesc)), TIMEOUT)
        val locationName = device.findObject(By.desc(locationNameContentDesc))
        Assert.assertEquals(MOUNTAIN_VIEW_CA, locationName.text)
    }

    @Test
    fun testNextDayClick() {
        getSolunarTimesByCurrentLocation()

        val locationNameContentDesc = UITestingUtil.getResourceString(R.string.location_name)
        device.wait(Until.hasObject(By.desc(locationNameContentDesc)), TIMEOUT)
        val dateContentDesc = UITestingUtil.getResourceString(R.string.date)
        val date = device.findObject(By.desc(dateContentDesc))
        val today = UITestingUtil.getResourceString(R.string.today)
        Assert.assertEquals(today, date.text)
        val nextDayContentDesc = UITestingUtil.getResourceString(R.string.next_day)
        device.findObject(By.desc(nextDayContentDesc)).click()
        device.wait(Until.hasObject(By.desc(dateContentDesc)), TIMEOUT)
        val tomorrow = UITestingUtil.getResourceString(R.string.tomorrow)
        Assert.assertEquals(tomorrow, date.text)
    }

    @Test
    fun testPreviousDayClick() {
        getSolunarTimesByCurrentLocation()

        val locationNameContentDesc = UITestingUtil.getResourceString(R.string.location_name)
        device.wait(Until.hasObject(By.desc(locationNameContentDesc)), TIMEOUT)
        val dateContentDesc = UITestingUtil.getResourceString(R.string.date)
        val date = device.findObject(By.desc(dateContentDesc))
        val today = UITestingUtil.getResourceString(R.string.today)
        Assert.assertEquals(today, date.text)
        val previousDayContentDesc = UITestingUtil.getResourceString(R.string.prev_day)
        device.findObject(By.desc(previousDayContentDesc)).click()
        device.wait(Until.hasObject(By.desc(dateContentDesc)), TIMEOUT)
        val yesterday = UITestingUtil.getResourceString(R.string.yesterday)
        Assert.assertEquals(yesterday, date.text)
    }

    private fun launchApp() {
        device = UiDevice.getInstance(instrumentation)
        device.pressHome()

        val context = instrumentation.context
        val packageName = instrumentation.targetContext.packageName
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)

        device.wait(Until.hasObject(By.pkg(packageName)), TIMEOUT)
    }

    private fun getSolunarTimesByCurrentLocation() {
        launchApp()
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(200).setBody(solunarSuccessResponse)
            }
        }
        device.findObject(
            By.text(
                UITestingUtil.getResourceString(R.string.get_current_location)
            )
        ).click()
        device.wait(Until.hasObject(By.text(WHILE_USING_THE_APP)), TIMEOUT)
        device.findObject(
            By.text(
                WHILE_USING_THE_APP
            )
        ).click()
    }
}
