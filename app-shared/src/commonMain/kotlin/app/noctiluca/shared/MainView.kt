package app.noctiluca.shared

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import noctiluca.designsystem.NoctilucaTheme
import noctiluca.features.timeline.SplashScreen

@Composable
fun MainView() = NoctilucaTheme {
    Navigator(SplashScreen)
}
