package noctiluca.features.shared.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

// @see: https://matsudamper.hatenablog.com/entry/2021/08/16/045025
fun Modifier.baseline(
    alignment: Alignment.Vertical,
    topOffset: Int = 0,
): Modifier {
    return then(
        layout { measurable, constraints ->
            val measued = measurable.measure(constraints)

            val baseline = -alignment.align(measued.height, topOffset)
            layout(
                measued.width, measued.height,
                mapOf(
                    FirstBaseline to baseline,
                    LastBaseline to baseline,
                )
            ) {
                measued.place(0, 0)
            }
        }
    )
}

fun Modifier.border(
    start: Dp? = null,
    top: Dp? = null,
    end: Dp? = null,
    bottom: Dp? = null,
    color: Color? = null,
) = composed {
    val borderColor = color ?: MaterialTheme.colorScheme.outline
    val (
        startWidth,
        topWidth,
        endWidth,
        bottomWidth,
    ) = with(LocalDensity.current) {
        listOf(
            start?.toPx(),
            top?.toPx(),
            end?.toPx(),
            bottom?.toPx(),
        )
    }

    Modifier.drawBehind {
        if (startWidth != null) {
            drawLine(
                borderColor,
                Offset(0F, 0F),
                Offset(0F, size.height),
                startWidth,
            )
        }
        if (topWidth != null) {
            drawLine(
                borderColor,
                Offset(0F, 0F),
                Offset(size.width, 0F),
                topWidth,
            )
        }
        if (endWidth != null) {
            drawLine(
                borderColor,
                Offset(size.width, 0F),
                Offset(size.width, size.height),
                endWidth,
            )
        }
        if (bottomWidth != null) {
            drawLine(
                borderColor,
                Offset(0F, size.height),
                Offset(size.width, size.height),
                bottomWidth,
            )
        }
    }
}
