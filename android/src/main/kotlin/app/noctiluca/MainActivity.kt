package app.noctiluca

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import app.noctiluca.navigation.redirectToSignIn
import noctiluca.components.utils.Browser
import noctiluca.theme.NoctilucaTheme

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            navController = rememberNavController()

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
