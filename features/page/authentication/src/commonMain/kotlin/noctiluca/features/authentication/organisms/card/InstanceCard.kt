package noctiluca.features.authentication.organisms.card

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.components.atoms.card.CardHeader
import noctiluca.components.atoms.card.CardSubhead
import noctiluca.components.atoms.card.CardSupporting
import noctiluca.components.atoms.card.FilledCard
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.model.QueryText
import noctiluca.features.authentication.state.rememberInstanceAndAuthorization

@Composable
internal fun InstanceCard(
    query: QueryText,
    onLoading: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (instance, authorizedUser) = rememberInstanceAndAuthorization(query)

    LaunchedEffect(authorizedUser.loading) {
        onLoading(authorizedUser.loading)
    }

    instance?.let {
        Box(modifier) {
            FilledCard(
                headline = { CardHeader(it.name) },
                subhead = { CardSubhead(it.domain) },
                supporting = { CardSupporting(it.description ?: "") },
                actions = {
                    AuthenticationButton(
                        !authorizedUser.loading,
                        onClick = { authorizedUser.requestAuthorize() },
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
