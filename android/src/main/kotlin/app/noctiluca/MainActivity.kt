package app.noctiluca

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import noctiluca.components.utils.Browser
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.authentication.model.invoke
import noctiluca.theme.NoctilucaTheme

class MainActivity : AppCompatActivity() {
    private val authorizeState: MutableState<AuthorizeResult?> by lazy { mutableStateOf(null) }
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
        authorizeState.value = AuthorizeResult(intent?.data)
    }
}
