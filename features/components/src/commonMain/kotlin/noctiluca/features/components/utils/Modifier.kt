package noctiluca.features.components.utils

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.layout.layout

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
