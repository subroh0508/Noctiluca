package app.noctiluca

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import cafe.adriel.voyager.navigator.Navigator
import noctiluca.designsystem.NoctilucaTheme
import noctiluca.features.timeline.Splash
import noctiluca.features.timeline.TimelineLaneScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NoctilucaTheme {
                Navigator(Splash)
            }
        }
    }
}
