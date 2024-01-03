package noctiluca.features.shared.utils

import kotlinx.datetime.LocalDateTime

internal const val FORMAT_MONTH_DAY = "MM/dd"
internal const val FORMAT_YEAR_MONTH_DAY = "yyyy/MM/dd"
internal const val FORMAT_YEAR_MONTH_DAY_TIME = "yyyy/MM/dd HH:mm:ss"

expect fun LocalDateTime.toMonthDay(): String
expect fun LocalDateTime.toYearMonthDay(): String
expect fun LocalDateTime.toYearMonthDayTime(): String
