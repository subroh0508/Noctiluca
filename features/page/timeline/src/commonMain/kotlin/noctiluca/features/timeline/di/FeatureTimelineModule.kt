package noctiluca.features.timeline.di

import noctiluca.features.shared.di.AuthorizedFeatureModule
import noctiluca.features.timeline.viewmodel.TimelinesViewModel
import noctiluca.timeline.domain.di.TimelineDomainModule
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.FeatureTimelineModule() {
    TimelineDomainModule()
    AuthorizedFeatureModule()
    factory { TimelinesViewModel(get(), get(), get(), get(), get(), get(), get()) }
}
