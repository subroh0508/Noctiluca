package noctiluca.features.shared.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import noctiluca.account.model.Account

@Composable
fun TooterName(
    tooter: Account,
    modifier: Modifier = Modifier,
    trailing: (@Composable (Modifier) -> Unit)? = null,
) = Column(modifier) {
    Row {
        Text(
            tooter.displayName,
            modifier = Modifier.weight(1F, true)
                .alignByBaseline(),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.titleMedium,
        )

        trailing?.let {
            Spacer(Modifier.width(16.dp))
            it.invoke(Modifier.alignByBaseline())
        }
    }

    Text(
        tooter.screen,
        color = MaterialTheme.colorScheme.outline,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        style = MaterialTheme.typography.titleSmall,
    )
}

@Composable
fun AccountName(
    displayName: String,
    screen: String,
    modifier: Modifier = Modifier,
    displayNameStyle: TextStyle = MaterialTheme.typography.titleLarge,
    usernameStyle: TextStyle = MaterialTheme.typography.titleMedium,
) = Column(modifier) {
    Text(
        displayName,
        style = displayNameStyle,
    )

    Text(
        screen,
        color = MaterialTheme.colorScheme.outline,
        style = usernameStyle,
    )
}

@Composable
fun AccountName(
    account: Account,
    modifier: Modifier = Modifier,
    displayNameStyle: TextStyle = MaterialTheme.typography.titleLarge,
    usernameStyle: TextStyle = MaterialTheme.typography.titleMedium,
) = AccountName(
    account.displayName,
    account.screen,
    modifier,
    displayNameStyle,
    usernameStyle,
)
