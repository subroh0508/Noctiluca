package noctiluca.features.signin.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import noctiluca.features.signin.getString
import noctiluca.model.authentication.Instance

@Composable
internal fun BoxScope.ActionButtons(
    instance: Instance?,
    isSignInProgress: Boolean,
    horizontalPadding: Dp,
    onClickAuthorize: (Instance) -> Unit,
) {
    instance ?: return

    Column(
        modifier = Modifier.fillMaxWidth()
            .align(Alignment.BottomCenter)
            .background(MaterialTheme.colorScheme.surface),
    ) {
        HorizontalDivider()

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(
                    vertical = 8.dp,
                    horizontal = horizontalPadding,
                ),
            horizontalArrangement = Arrangement.End
        ) { AuthorizeButton(instance, isSignInProgress, onClickAuthorize) }
    }
}

@Composable
private fun AuthorizeButton(
    instance: Instance,
    isSignInProgress: Boolean,
    onClickAuthorize: (Instance) -> Unit,
) {
    if (isSignInProgress) {
        OutlinedButton(
            onClick = {},
            enabled = false,
        ) { CircularProgressIndicator(Modifier.size(20.dp)) }

        return
    }

    Button(
        onClick = { onClickAuthorize(instance) },
    ) { Text(getString().sign_in_request_authentication) }
}
