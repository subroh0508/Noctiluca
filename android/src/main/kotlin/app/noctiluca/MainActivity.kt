package app.noctiluca

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.util.Consumer
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import noctiluca.features.authentication.*
import noctiluca.features.authentication.R
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.authentication.model.invoke
import noctiluca.theme.NoctilucaTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NoctilucaTheme {
                SignInScreen { HandleOnNewIntent(it) }
            }
        }
    }

    // @see: https://github.com/adrielcafe/voyager/issues/149
    @Composable
    fun HandleOnNewIntent(navigator: Navigator) {
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            callbackFlow<Intent> {
                val activity = context as? ComponentActivity
                val consumer = Consumer<Intent> { trySend(it) }

                activity?.addOnNewIntentListener(consumer)
                awaitClose { activity?.removeOnNewIntentListener(consumer) }
            }.collectLatest { intent ->
                val uri = intent.data ?: return@collectLatest

                when (uri.scheme) {
                    getString(R.string.sign_in_oauth_scheme) -> navigator.push(
                        ScreenRegistry.get(
                            SignInScreen.MastodonInstanceDetail(
                                uri.host ?: return@collectLatest,
                                AuthorizeResult(uri.query ?: return@collectLatest),
                            ),
                        ),
                    )
                }
            }
        }
    }
}
