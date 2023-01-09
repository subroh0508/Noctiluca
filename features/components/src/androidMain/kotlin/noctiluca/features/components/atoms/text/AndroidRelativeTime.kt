package noctiluca.features.components.atoms.text

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter

internal actual fun LocalDateTime.toMonthDay() = DateTimeFormatter.ofPattern(FORMAT_MONTH_DAY).format(this.toJavaLocalDateTime())
internal actual fun LocalDateTime.toYearMonthDay() = DateTimeFormatter.ofPattern(FORMAT_YEAR_MONTH_DAY).format(this.toJavaLocalDateTime())
