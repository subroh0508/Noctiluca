package noctiluca.features.authentication

import androidx.compose.runtime.*
import androidx.compose.ui.text.intl.Locale
import noctiluca.components.FeatureComponent
import noctiluca.features.authentication.di.SignInModule
import noctiluca.features.authentication.organisms.SearchInstanceList

internal val CurrentScope get() = SignInModule.scope
internal val LocalResources = compositionLocalOf { Resources("JA") }

@Composable
fun SignInCompose() = FeatureComponent(SignInModule) {
    CompositionLocalProvider(LocalResources provides Resources(Locale.current.language)) {
        SearchInstanceList()
    }
}
