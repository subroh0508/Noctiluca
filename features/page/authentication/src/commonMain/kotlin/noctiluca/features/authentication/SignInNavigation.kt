package noctiluca.features.authentication

import noctiluca.features.components.Navigation

interface SignInNavigation : Navigation {
    fun navigateToTimeline()
    fun navigateToInstanceDetail(domain: String)
}
