package com.timserio.select_location_presentation

import com.google.android.gms.maps.model.LatLng
import com.timserio.test_utils.MainDispatcherRule
import com.timserio.test_utils.TestConstants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

class SelectLocationViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: SelectLocationViewModel

    @Before
    fun setup() {
        viewModel = SelectLocationViewModel()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Handle cancel click event test`() = runTest {
        // WHEN: handleEvent(SelectLocationEvent.OnCancelClick) is called
        viewModel.state.handleEvent(SelectLocationEvent.OnCancelClick)

        val expected = SelectLocationState(
            isLocationSelected = false,
            selectedLocation = null
        )
        // THEN: Then isLocationSelected is false and selectedLocation is null
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Handle info window long click event test`() = runTest {
        // WHEN: handleEvent(SelectLocationEvent.OnInfoWindowLongClick) is called
        viewModel.state.handleEvent(SelectLocationEvent.OnInfoWindowLongClick)

        val expected = SelectLocationState(
            isLocationSelected = false,
            selectedLocation = null
        )
        // THEN: Then isLocationSelected is false and selectedLocation is null
        Assert.assertEquals(expected, viewModel.state.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Handle select button click event test`() = runTest {
        // WHEN: handleEvent(SelectLocationEvent.OnSelectBtnClick) is called
        val selectLocationEvent = mock<() -> Unit>({})
        viewModel.state.handleEvent(SelectLocationEvent.OnSelectBtnClick(selectLocationEvent))

        // THEN: Then selectLocationEvent was invoked once
        Mockito.verify(selectLocationEvent, Mockito.times(1)).invoke()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Handle map long click event test`() = runTest {
        // WHEN: handleEvent(SelectLocationEvent.OnMapLongClick) is called
        val latLong = LatLng(TestConstants.LATITUDE, TestConstants.LONGITUDE)
        viewModel.state.handleEvent(SelectLocationEvent.OnMapLongClick(latLong))

        val expected = SelectLocationState(
            isLocationSelected = true,
            selectedLocation = latLong
        )
        // THEN: Then isLocationSelected is true and selectedLocation is set to expected LatLng object
        Assert.assertEquals(expected, viewModel.state.value)
    }
}
