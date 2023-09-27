package com.timserio.home_presentation

import java.time.LocalDate

data class HomeState(
    val isLoading: Boolean = true,
    val latLong: Pair<Double, Double>? = null,
    val timeZone: Int = -5,
    val locationName: String = "",
    val date: LocalDate = LocalDate.now(),
    val majorOne: String = "",
    val majorTwo: String = "",
    val minorOne: String = "",
    val minorTwo: String = "",
    val dayRating: Pair<Int, Float> = Pair(-1, 0f),
    val isLocationRequestSuccessful: Boolean? = null,
    val isSolunarResponseSuccessful: Boolean? = null
)
