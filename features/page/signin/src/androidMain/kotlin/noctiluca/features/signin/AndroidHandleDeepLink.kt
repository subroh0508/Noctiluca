package noctiluca.features.signin

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.util.Consumer
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import noctiluca.features.navigation.redirectToSignIn

// @see: https://github.com/adrielcafe/voyager/issues/149
@Composable
actual fun HandleDeepLink() {
    val navigator = LocalNavigator.current
    val context = LocalContext.current
    val oauthScheme = stringResource(R.string.sign_in_oauth_scheme)

    LaunchedEffect(Unit) {
        callbackFlow<Intent> {
            val activity = context as? ComponentActivity
            val consumer = Consumer<Intent> { trySend(it) }

            activity?.addOnNewIntentListener(consumer)
            awaitClose { activity?.removeOnNewIntentListener(consumer) }
        }.collectLatest { intent ->
            val uri = intent.data ?: return@collectLatest

            when (uri.scheme) {
                oauthScheme -> navigator?.redirectToSignIn(uri)
            }
        }
    }
}
