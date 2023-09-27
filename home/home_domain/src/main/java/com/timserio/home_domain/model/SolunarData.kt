package com.timserio.home_domain.model

data class SolunarData(
    val majorOneStart: String? = null,
    val majorOneEnd: String? = null,
    val majorTwoStart: String? = null,
    val majorTwoEnd: String? = null,
    val minorOneStart: String? = null,
    val minorOneEnd: String? = null,
    val minorTwoStart: String? = null,
    val minorTwoEnd: String? = null,
    val hourlyRatings: List<Int>? = null
)
