package app.noctiluca

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import noctiluca.features.timeline.FeatureTimelineScreen
import noctiluca.designsystem.NoctilucaTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NoctilucaTheme {
                FeatureTimelineScreen()
            }
        }
    }
}
