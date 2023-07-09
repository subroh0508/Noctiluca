package app.noctiluca

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import app.noctiluca.navigation.AndroidNavigator
import noctiluca.theme.NoctilucaTheme

class MainActivity : AppCompatActivity() {
    private val navigator by lazy { AndroidNavigator(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NoctilucaTheme {
                Routing(navigator)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val uri = intent?.data ?: return

        when (uri.scheme) {
            getString(R.string.sign_in_oauth_scheme) -> navigator.redirectToSignIn(uri)
        }
    }
}
