package noctiluca.features.shared.components.text

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import kotlinx.datetime.*
import noctiluca.features.shared.StringResources
import noctiluca.features.shared.getString
import noctiluca.features.shared.utils.format
import noctiluca.features.shared.utils.toMonthDay
import noctiluca.features.shared.utils.toYearMonthDay

@Composable
fun RelativeTime(
    dateTime: LocalDateTime,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    style: TextStyle = LocalTextStyle.current,
) = Text(buildTimestamp(dateTime, res = getString()), modifier, color = color, style = style)

fun buildTimestamp(
    dateTime: LocalDateTime,
    now: Instant = Clock.System.now(),
    timeZone: TimeZone = TimeZone.of("Asia/Tokyo"),
    res: StringResources,
): String {
    val diff = (now - dateTime.toInstant(timeZone)).inWholeSeconds.toInt()

    return when {
        diff < DIFF_NOW -> res.time_now
        diff < DIFF_SECONDS -> res.time_seconds.format(diff)
        diff < DIFF_MINUTES -> res.time_minutes.format(diff / DIFF_SECONDS)
        diff < DIFF_HOURS -> res.time_hours.format(diff / DIFF_MINUTES)
        diff < DIFF_DAYS -> res.time_days.format(diff / DIFF_HOURS)
        else -> date(dateTime, now.toLocalDateTime(timeZone).year)
    }
}

private const val DIFF_NOW = 1
private const val DIFF_SECONDS = 60
private const val DIFF_MINUTES = 3_600
private const val DIFF_HOURS = 86_400
private const val DIFF_DAYS = 2_592_000

private fun date(dateTime: LocalDateTime, nowYear: Int): String {
    if (dateTime.year == nowYear) {
        return dateTime.toMonthDay()
    }

    return dateTime.toYearMonthDay()
}
