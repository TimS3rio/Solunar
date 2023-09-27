package com.timserio.home_data.mapper

import com.timserio.home_data.remote.dto.SolunarResponse
import com.timserio.home_domain.model.SolunarData

fun SolunarResponse.toSolunarData(): SolunarData {
    val hourlyRatings = hourlyRating?.let {
        listOf(
            it.zero ?: 0, it.one ?: 0, it.two ?: 0, it.three ?: 0,
            it.four ?: 0, it.five ?: 0, it.six ?: 0, it.seven ?: 0,
            it.eight ?: 0, it.nine ?: 0, it.ten ?: 0, it.eleven ?: 0,
            it.twelve ?: 0, it.thirteen ?: 0, it.fourteen ?: 0, it.fifteen ?: 0,
            it.sixteen ?: 0, it.seventeen ?: 0, it.eighteen ?: 0, it.nineteen ?: 0,
            it.twenty ?: 0, it.twentyOne ?: 0, it.twentyTwo ?: 0, it.twentyThree ?: 0
        )
    } ?: run {
        null
    }
    return SolunarData(
        majorOneStart = major1Start,
        majorOneEnd = major1Stop,
        majorTwoStart = major2Start,
        majorTwoEnd = major2Stop,
        minorOneStart = minor1Start,
        minorOneEnd = minor1Stop,
        minorTwoStart = minor2Start,
        minorTwoEnd = minor2Stop,
        hourlyRatings = hourlyRatings
    )
}
