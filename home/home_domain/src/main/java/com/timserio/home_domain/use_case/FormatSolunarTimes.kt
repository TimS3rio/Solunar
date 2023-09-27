package com.timserio.home_domain.use_case

import com.timserio.home_domain.model.FormattedSolunarTimes
import com.timserio.home_domain.model.SolunarData
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class FormatSolunarTimes {

    companion object {
        private const val INPUT_TIME_PATTERN = "HH:mm"
        private const val OUTPUT_TIME_PATTERN = "h:mm a"
    }

    operator fun invoke(data: SolunarData): FormattedSolunarTimes {
        return FormattedSolunarTimes(
            formatTimeRange(data.majorOneStart, data.majorOneEnd),
            formatTimeRange(data.majorTwoStart, data.majorTwoEnd),
            formatTimeRange(data.minorOneStart, data.minorOneEnd),
            formatTimeRange(data.minorTwoStart, data.minorTwoEnd)
        )
    }

    private fun formatTimeRange(startOfRange: String?, endOfRange: String?): String {
        val formattedStartOfRange = startOfRange?.let { formatTimeText(startOfRange) } ?: run { "" }
        val formattedEndOfRange = endOfRange?.let { formatTimeText(endOfRange) } ?: run { "" }
        return if (formattedStartOfRange.isBlank() || formattedEndOfRange.isBlank()) {
            ""
        } else {
            "$formattedStartOfRange - $formattedEndOfRange"
        }
    }

    private fun formatTimeText(timeText: String?): String {
        return try {
            timeText?.let {
                val inputSDF = SimpleDateFormat(INPUT_TIME_PATTERN, Locale.getDefault())
                val outputSDF = SimpleDateFormat(OUTPUT_TIME_PATTERN, Locale.getDefault())
                val inputDate = inputSDF.parse(timeText)
                inputDate?.let {
                    outputSDF.format(it)
                } ?: run {
                    ""
                }
            } ?: run {
                ""
            }
        } catch (e: ParseException) {
            ""
        }
    }
}
