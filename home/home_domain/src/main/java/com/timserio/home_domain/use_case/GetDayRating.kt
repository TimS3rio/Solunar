package com.timserio.home_domain.use_case

import kotlin.math.roundToInt

class GetDayRating {
    operator fun invoke(hourlyRatings: List<Int>): Pair<Int, Float> {
        val result = hourlyRatings.sum() / 900f
        return Pair((result * 100).roundToInt().coerceIn(0..100), result.coerceIn(0f, 1f))
    }
}
