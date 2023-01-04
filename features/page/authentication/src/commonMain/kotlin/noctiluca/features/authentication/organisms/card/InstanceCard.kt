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
import noctiluca.components.atoms.snackbar.showSnackbar
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.model.QueryText
import noctiluca.features.authentication.state.AuthorizedUserState
import noctiluca.features.authentication.state.rememberInstanceAndAuthorization
import noctiluca.instance.model.Instance

@Composable
internal fun InstanceCard(
    query: QueryText,
    onLoading: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (instance, authorizedUser) = rememberInstanceAndAuthorization(query)

    LaunchedEffect(authorizedUser.loading) { onLoading(authorizedUser.loading) }

    MastodonInstanceCard(instance ?: return, authorizedUser, modifier)
    SnackbarForError(authorizedUser.getErrorOrNull() ?: return)
}

@Composable
private fun MastodonInstanceCard(
    instance: Instance,
    authorizedUserState: AuthorizedUserState,
    modifier: Modifier = Modifier,
) = Box(modifier) {
    FilledCard(
        headline = { CardHeader(instance.name) },
        subhead = { CardSubhead(instance.domain) },
        supporting = { CardSupporting(instance.description ?: "") },
        actions = {
            AuthenticationButton(
                !authorizedUserState.loading,
                onClick = { authorizedUserState.requestAuthorize() },
            )
        },
        modifier = Modifier.padding(vertical = 16.dp),
    )
}

@Composable
private fun AuthenticationButton(
    enabled: Boolean,
    onClick: () -> Unit,
) = Button(
    enabled = enabled,
    onClick = onClick,
) { Text(getString().sign_in_request_authentication) }

@Composable
private fun SnackbarForError(
    error: Throwable,
) {
    val message = getString().sign_in_request_authentication_error + (error.message ?: "Unknown Error")

    showSnackbar(message)
}
