package noctiluca.data.extensions

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format

internal fun LocalDateTime.toISO8601() = format(LocalDateTime.Formats.ISO)
