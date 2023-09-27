package com.timserio.home_domain.use_case

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class FormatDateForRequest {

    companion object {
        private const val OUTPUT_DATE_PATTERN = "yyyyMMdd"
    }

    operator fun invoke(date: LocalDate): String {
        val outputSDF = DateTimeFormatter.ofPattern(OUTPUT_DATE_PATTERN, Locale.getDefault())
        return outputSDF.format(date)
    }
}
