package app.noctiluca

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController
import app.noctiluca.navigation.AndroidNavigationController
import noctiluca.features.components.utils.Browser
import noctiluca.theme.NoctilucaTheme

class MainActivity : AppCompatActivity() {
    private lateinit var navController: AndroidNavigationController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            navController = AndroidNavigationController(
                rememberNavController(),
                this,
            )

            NoctilucaTheme {
                Routing(Browser(this), navController)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val uri = intent?.data ?: return

        when (uri.scheme) {
            getString(R.string.sign_in_oauth_scheme) -> navController.redirectToSignIn(uri)
        }
    }
}
