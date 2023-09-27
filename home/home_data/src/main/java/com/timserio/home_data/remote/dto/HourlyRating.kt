package com.timserio.home_data.remote.dto

import com.squareup.moshi.Json

data class HourlyRating(
    @field:Json(name = "0") var zero: Int? = null,
    @field:Json(name = "1") var one: Int? = null,
    @field:Json(name = "2") var two: Int? = null,
    @field:Json(name = "3") var three: Int? = null,
    @field:Json(name = "4") var four: Int? = null,
    @field:Json(name = "5") var five: Int? = null,
    @field:Json(name = "6") var six: Int? = null,
    @field:Json(name = "7") var seven: Int? = null,
    @field:Json(name = "8") var eight: Int? = null,
    @field:Json(name = "9") var nine: Int? = null,
    @field:Json(name = "10") var ten: Int? = null,
    @field:Json(name = "11") var eleven: Int? = null,
    @field:Json(name = "12") var twelve: Int? = null,
    @field:Json(name = "13") var thirteen: Int? = null,
    @field:Json(name = "14") var fourteen: Int? = null,
    @field:Json(name = "15") var fifteen: Int? = null,
    @field:Json(name = "16") var sixteen: Int? = null,
    @field:Json(name = "17") var seventeen: Int? = null,
    @field:Json(name = "18") var eighteen: Int? = null,
    @field:Json(name = "19") var nineteen: Int? = null,
    @field:Json(name = "20") var twenty: Int? = null,
    @field:Json(name = "21") var twentyOne: Int? = null,
    @field:Json(name = "22") var twentyTwo: Int? = null,
    @field:Json(name = "23") var twentyThree: Int? = null
)
