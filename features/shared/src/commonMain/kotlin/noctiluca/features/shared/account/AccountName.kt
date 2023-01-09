package noctiluca.features.shared.account

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import noctiluca.account.model.Account
import noctiluca.status.model.Tooter

@Composable
fun TooterName(
    tooter: Tooter,
    modifier: Modifier = Modifier
) = Column(modifier) {
    Text(
        tooter.displayName,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        style = MaterialTheme.typography.titleMedium,
    )

    Text(
        "@${tooter.username}",
        color = MaterialTheme.colorScheme.outline,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        style = MaterialTheme.typography.titleSmall,
    )
}

@Composable
fun AccountName(
    account: Account,
    modifier: Modifier = Modifier,
) = Column(modifier) {
    Text(
        account.displayName,
        style = MaterialTheme.typography.titleLarge,
    )

    Text(
        "@${account.username}",
        color = MaterialTheme.colorScheme.outline,
        style = MaterialTheme.typography.titleMedium,
    )
}
