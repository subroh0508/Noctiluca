package noctiluca.features.authentication

import androidx.compose.runtime.*
import noctiluca.components.FeatureComponent
import noctiluca.features.authentication.di.SignInModule
import noctiluca.features.authentication.organisms.SearchInstanceList

val CurrentScope get() = SignInModule.scope

@Composable
fun SignInCompose() = FeatureComponent(SignInModule) {
    SearchInstanceList()
}