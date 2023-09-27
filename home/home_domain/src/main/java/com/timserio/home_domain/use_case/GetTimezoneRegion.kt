package com.timserio.home_domain.use_case

import java.util.TimeZone

class GetTimezoneRegion {
    companion object {
        private const val HOUR_IN_MILLISECONDS = 3600000
    }

    operator fun invoke(): Int {
        val timeZone = TimeZone.getDefault()
        if (timeZone.useDaylightTime()) {
            return (timeZone.rawOffset + timeZone.dstSavings) / HOUR_IN_MILLISECONDS
        }
        return timeZone.rawOffset / HOUR_IN_MILLISECONDS
    }
}
