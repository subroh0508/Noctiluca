package app.noctiluca

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import noctiluca.features.authentication.SignIn
import noctiluca.theme.NoctilucaTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoctilucaTheme {
                SignIn()
            }
        }
    }
}