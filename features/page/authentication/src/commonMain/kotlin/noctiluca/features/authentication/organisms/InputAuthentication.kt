package noctiluca.features.authentication.organisms

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import noctiluca.components.atoms.card.CardHeader
import noctiluca.components.atoms.card.CardSubhead
import noctiluca.components.atoms.card.CardSupporting
import noctiluca.components.atoms.card.FilledCard
import noctiluca.components.atoms.textfield.SingleLineTextField
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.state.rememberMastodonInstance
import noctiluca.instance.model.Instance

private val padding = 16.dp

@Composable
fun InputAuthentication(
    domain: String?,
    modifier: Modifier = Modifier,
) {
    domain ?: return

    val state by rememberMastodonInstance(domain)

    val instance: Instance = state.getValueOrNull() ?: return

    InputAuthenticationLayout(instance, modifier)
}

@Composable
internal fun InputAuthenticationLayout(
    instance: Instance,
    modifier: Modifier = Modifier,
) = Column(modifier) {
    FilledCard(
        headline = { CardHeader(instance.name) },
        subhead = { CardSubhead(instance.domain) },
        supporting = { CardSupporting(instance.description ?: "") },
        actions = { AuthenticationButton() },
        modifier = Modifier.padding(padding),
    )
}

@Composable
private fun AuthenticationButton() {
    Button(onClick = {}) { Text(getString().sign_in_request_authentication) }
}
