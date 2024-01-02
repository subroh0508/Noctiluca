package noctiluca.features.timeline.di

import noctiluca.features.timeline.viewmodel.TimelinesViewModel
import noctiluca.timeline.domain.di.TimelineDomainModule
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.FeatureTimelineModule() {
    TimelineDomainModule()
    factory { TimelinesViewModel(get(), get(), get(), get(), get(), get()) }
}
