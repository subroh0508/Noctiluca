package noctiluca.features.authentication.organisms.card

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.components.atoms.card.CardHeader
import noctiluca.components.atoms.card.CardSubhead
import noctiluca.components.atoms.card.CardSupporting
import noctiluca.components.atoms.card.FilledCard
import noctiluca.components.utils.openBrowser
import noctiluca.features.authentication.LocalAuthorizeCode
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.model.QueryText
import noctiluca.features.authentication.state.rememberAuthorizeUri
import noctiluca.features.authentication.state.rememberMastodonInstance
import noctiluca.instance.model.Instance

@Composable
internal fun InstanceCard(
    query: QueryText,
    onLoading: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val instance by rememberMastodonInstance(query)
    val authorizeUri = rememberAuthorizeUri()

    LaunchedEffect(instance.loading, authorizeUri.loading) {
        onLoading(instance.loading || authorizeUri.loading)
    }

    val authorizeCode = LocalAuthorizeCode.current
    if (authorizeCode == null) {
        authorizeUri.getValueOrNull()?.let { openBrowser(it) }
    }

    instance.getValueOrNull<Instance>()?.let {
        Box(modifier) {
            FilledCard(
                headline = { CardHeader(it.name) },
                subhead = { CardSubhead(it.domain) },
                supporting = { CardSupporting(it.description ?: "") },
                actions = {
                    AuthenticationButton(
                        authorizeCode == null,
                        onClick = { authorizeUri.request(it) },
                    )
                },
                modifier = Modifier.padding(vertical = 16.dp),
            )
        }
    }
}

@Composable
private fun AuthenticationButton(
    enabled: Boolean,
    onClick: () -> Unit,
) = Button(
    enabled = enabled,
    onClick = onClick,
) { Text(getString().sign_in_request_authentication) }
