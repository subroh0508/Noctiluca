package noctiluca.features.toot.di

import noctiluca.features.toot.TootScreen
import noctiluca.features.toot.viewmodel.TootViewModel
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.FeatureTootModule() {
    scope<TootScreen> {
        scoped { TootViewModel(get(), get(), get()) }
    }
}
