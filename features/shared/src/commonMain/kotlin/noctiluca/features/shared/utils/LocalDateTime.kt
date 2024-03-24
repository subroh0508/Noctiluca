package noctiluca.features.shared.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern

private const val FORMAT_MONTH_DAY = "MM/dd"
private const val FORMAT_YEAR_MONTH_DAY = "yyyy/MM/dd"
private const val FORMAT_YEAR_MONTH_DAY_TIME = "yyyy/MM/dd HH:mm:ss"

@OptIn(FormatStringsInDatetimeFormats::class)
private val MonthDayFormat = LocalDateTime.Format { byUnicodePattern(FORMAT_MONTH_DAY) }

@OptIn(FormatStringsInDatetimeFormats::class)
private val YearMonthDayFormat = LocalDateTime.Format { byUnicodePattern(FORMAT_YEAR_MONTH_DAY) }

@OptIn(FormatStringsInDatetimeFormats::class)
private val YearMonthDayTimeFormat =
    LocalDateTime.Format { byUnicodePattern(FORMAT_YEAR_MONTH_DAY_TIME) }

fun LocalDateTime.toMonthDay() = format(MonthDayFormat)
fun LocalDateTime.toYearMonthDay() = format(YearMonthDayFormat)
fun LocalDateTime.toYearMonthDayTime() = format(YearMonthDayTimeFormat)
