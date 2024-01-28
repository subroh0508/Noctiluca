package noctiluca.features.accountdetail.component.caption

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.utils.baseline
import noctiluca.features.shared.utils.toDp
import noctiluca.model.accountdetail.AccountAttributes

@Composable
internal fun AccountName(
    detail: AccountAttributes,
    modifier: Modifier = Modifier,
) = Column(modifier) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            detail.displayName,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1F),
        )

        if (detail.bot) {
            BotLabel(
                modifier = Modifier.align(Alignment.Bottom),
            )
        }
    }

    Row {
        val baselineModifier = remember { Modifier.alignByBaseline() }

        Text(
            detail.screen,
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.titleMedium,
            modifier = baselineModifier,
        )

        if (detail.locked) {
            LockIcon(
                baselineModifier = baselineModifier,
            )
        }
    }
}

@Composable
private fun BotLabel(
    modifier: Modifier = Modifier,
) = Row(
    modifier = Modifier.border(
        width = 1.dp,
        color = MaterialTheme.colorScheme.outline,
        shape = AssistChipDefaults.shape,
    )
        .padding(vertical = 4.dp, horizontal = 8.dp)
        .then(modifier),
) {
    val baselineModifier = remember { Modifier.alignByBaseline() }

    Icon(
        Icons.Default.SmartToy,
        contentDescription = "Bot",
        modifier = Modifier.size(MaterialTheme.typography.titleSmall.fontSize.toDp())
            .baseline(Alignment.Bottom, topOffset = 6)
            .then(baselineModifier),
    )

    Spacer(modifier = Modifier.width(4.dp))

    Text(
        "Bot",
        style = MaterialTheme.typography.titleSmall,
        modifier = baselineModifier,
    )
}

@Composable
private fun LockIcon(
    baselineModifier: Modifier,
) = Icon(
    Icons.Default.Lock,
    contentDescription = null,
    tint = MaterialTheme.colorScheme.outline,
    modifier = Modifier.baseline(Alignment.Bottom, topOffset = 4)
        .then(baselineModifier)
        .padding(start = 4.dp)
        .size(MaterialTheme.typography.titleMedium.fontSize.toDp()),
)
