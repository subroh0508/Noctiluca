package noctiluca.features.shared.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter

actual fun LocalDateTime.toMonthDay(): String =
    DateTimeFormatter.ofPattern(FORMAT_MONTH_DAY).format(this.toJavaLocalDateTime())

actual fun LocalDateTime.toYearMonthDay(): String =
    DateTimeFormatter.ofPattern(FORMAT_YEAR_MONTH_DAY).format(this.toJavaLocalDateTime())

actual fun LocalDateTime.toYearMonthDayTime(): String =
    DateTimeFormatter.ofPattern(FORMAT_YEAR_MONTH_DAY_TIME).format(this.toJavaLocalDateTime())
