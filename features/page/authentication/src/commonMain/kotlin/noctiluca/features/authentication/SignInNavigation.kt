package noctiluca.features.authentication

import noctiluca.features.components.Navigation

interface SignInNavigation : Navigation {
    fun navigateToTimelines()
    fun navigateToInstanceDetail(domain: String)
}
