package noctiluca.features.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed class TimelineScreen : ScreenProvider {
    data object Timelines : TimelineScreen()
    data object Toot : TimelineScreen()
}
