package noctiluca.features.components.atoms.text

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import kotlinx.datetime.*
import noctiluca.features.components.getString
import okhttp3.internal.format

@Composable
fun RelativeTime(
    dateTime: LocalDateTime,
    timeZone: TimeZone = TimeZone.of("Asia/Tokyo"),
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    style: TextStyle = LocalTextStyle.current,
) {
    val now = Clock.System.now()
    val diff = (now - dateTime.toInstant(timeZone)).inWholeSeconds.toInt()

    val timestamp = when {
        diff < 1 -> getString().time_now
        diff < 60 -> seconds(diff)
        diff < 3_600 -> minutes(diff / 60)
        diff < 86_400 -> hours(diff / 3_600)
        diff < 2_592_000 -> days(diff / 86_400)
        else -> date(dateTime, now.toLocalDateTime(timeZone).year)
    }

    Text(timestamp, modifier, color = color, style = style)
}

@Composable
private fun seconds(n: Int) = format(getString().time_seconds, n)

@Composable
private fun minutes(n: Int) = format(getString().time_minutes, n)

@Composable
private fun hours(n: Int) = format(getString().time_hours, n)

@Composable
private fun days(n: Int) = format(getString().time_days, n)

private fun date(dateTime: LocalDateTime, nowYear: Int): String {
    if (dateTime.year == nowYear) {
        return dateTime.toMonthDay()
    }

    return dateTime.toYearMonthDay()
}

internal const val FORMAT_MONTH_DAY = "MM/dd"
internal const val FORMAT_YEAR_MONTH_DAY = "yyyy/MM/dd"

internal expect fun LocalDateTime.toMonthDay(): String
internal expect fun LocalDateTime.toYearMonthDay(): String

