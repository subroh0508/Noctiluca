package noctiluca.features.shared.status.header

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.getCommonString
import noctiluca.features.shared.utils.format
import noctiluca.model.account.Account

@Composable
internal fun RebloggedBy(
    rebloggedBy: Account?,
) {
    rebloggedBy ?: return

    CompositionLocalProvider(
        LocalContentColor provides MaterialTheme.colorScheme.outline,
    ) {
        Row(
            modifier = Modifier.padding(bottom = 8.dp),
        ) {
            Box(
                modifier = Modifier.width(60.dp)
                    .padding(end = 16.dp),
            ) {
                Icon(
                    Icons.Default.Repeat,
                    contentDescription = "Reblogged By",
                    modifier = Modifier.size(20.dp)
                        .align(Alignment.CenterEnd),
                )
            }

            Text(
                getCommonString().status_reblogged_by.format(rebloggedBy.displayName),
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}
