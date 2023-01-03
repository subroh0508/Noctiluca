package app.noctiluca

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import noctiluca.components.utils.Browser
import noctiluca.features.authentication.model.AuthorizeCode
import noctiluca.features.authentication.model.invoke
import noctiluca.theme.NoctilucaTheme

class MainActivity : AppCompatActivity() {
    private val authorizeState: MutableState<AuthorizeCode?> by lazy { mutableStateOf(null) }
    private val browser by lazy { Browser(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NoctilucaTheme {
                Routing(authorizeState.value, browser)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        authorizeState.value = AuthorizeCode(intent?.data)
    }
}
