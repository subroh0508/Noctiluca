package noctiluca.features.authentication.templates.scaffold.instancedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import noctiluca.features.authentication.LocalAuthorizeResult
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.state.rememberAuthorizedUser
import noctiluca.instance.model.Instance

@Composable
internal fun BoxScope.InstanceDetailActionButtons(
    instance: Instance,
    horizontalPadding: Dp,
) = Column(
    modifier = Modifier.fillMaxWidth()
        .align(Alignment.BottomCenter)
        .background(MaterialTheme.colorScheme.surface),
) {
    Divider(Modifier.fillMaxWidth())

    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(
                vertical = 8.dp,
                horizontal = horizontalPadding,
            ),
        horizontalArrangement = Arrangement.End
    ) { AuthorizeButton(instance) }
}

@Composable
private fun AuthorizeButton(
    instance: Instance,
) {
    val authorizedUserState = rememberAuthorizedUser(instance.domain)
    val isSignInProgress = LocalAuthorizeResult.current
        ?.getCodeOrNull() != null && authorizedUserState.loading

    if (isSignInProgress) {
        OutlinedButton(
            onClick = {},
            enabled = false,
        ) { CircularProgressIndicator(Modifier.size(20.dp)) }

        return
    }

    val scope = rememberCoroutineScope()

    Button(
        onClick = { authorizedUserState.requestAuthorize(scope, instance) },
    ) { Text(getString().sign_in_request_authentication) }
}
