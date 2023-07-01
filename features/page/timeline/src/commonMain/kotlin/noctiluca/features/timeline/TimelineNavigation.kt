package noctiluca.features.timeline

import noctiluca.features.components.Navigation

interface TimelineNavigation : Navigation {
    fun navigateToAccountDetail(id: String)
    fun navigateToToot()
}
