package noctiluca.features.components.utils

import kotlinx.datetime.LocalDateTime

internal const val FORMAT_MONTH_DAY = "MM/dd"
internal const val FORMAT_YEAR_MONTH_DAY = "yyyy/MM/dd"

expect fun LocalDateTime.toMonthDay(): String
expect fun LocalDateTime.toYearMonthDay(): String
