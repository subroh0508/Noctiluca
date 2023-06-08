package app.noctiluca

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController
import app.noctiluca.navigation.AndroidNavigation
import noctiluca.theme.NoctilucaTheme

class MainActivity : AppCompatActivity() {
    private lateinit var navigation: AndroidNavigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            navigation = AndroidNavigation(
                rememberNavController(),
                this,
            )

            NoctilucaTheme {
                Routing(navigation)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val uri = intent?.data ?: return

        when (uri.scheme) {
            getString(R.string.sign_in_oauth_scheme) -> navigation.redirectToSignIn(uri)
        }
    }
}
