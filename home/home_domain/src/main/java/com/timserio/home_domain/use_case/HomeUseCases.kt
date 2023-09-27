package com.timserio.home_domain.use_case

data class HomeUseCases(
    val formatDateForRequest: FormatDateForRequest,
    val formatSolunarTimes: FormatSolunarTimes,
    val getSolunarTimes: GetSolunarTimes,
    val getTimezoneRegion: GetTimezoneRegion,
    val getLocationName: GetLocationName,
    val getDayRating: GetDayRating
)
