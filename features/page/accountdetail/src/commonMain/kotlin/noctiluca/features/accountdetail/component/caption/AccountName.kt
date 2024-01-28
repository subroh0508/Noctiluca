package noctiluca.features.accountdetail.component.caption

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
    Text(
        detail.displayName,
        style = MaterialTheme.typography.titleLarge,
    )

    Row {
        val baselineModifier = remember { Modifier.alignByBaseline() }

        Text(
            detail.screen,
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.titleMedium,
            modifier = baselineModifier,
        )

        if (detail.locked) {
            Icon(
                Icons.Default.Lock,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.baseline(Alignment.Bottom, topOffset = 4)
                    .then(baselineModifier)
                    .padding(start = 4.dp)
                    .size(MaterialTheme.typography.titleMedium.fontSize.toDp()),
            )
        }
    }
}
