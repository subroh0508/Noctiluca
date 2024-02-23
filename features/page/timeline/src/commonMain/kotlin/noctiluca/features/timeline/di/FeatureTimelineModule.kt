package noctiluca.features.timeline.di

import noctiluca.data.di.AuthorizedContext
import noctiluca.features.timeline.TimelineLaneScreen
import noctiluca.features.timeline.TootScreen
import noctiluca.features.timeline.viewmodel.AuthorizedAccountViewModel
import noctiluca.features.timeline.viewmodel.TimelinesViewModel
import noctiluca.timeline.domain.di.TimelineDomainModule
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.FeatureTimelineModule() {
    scope<AuthorizedContext> {
        TimelineDomainModule()
    }

    scope<TimelineLaneScreen> {
        scoped { AuthorizedAccountViewModel(get(), get()) }
        scoped { TimelinesViewModel(get(), get(), get(), get(), get()) }
    }

    scope<TootScreen> {
        scoped { TimelinesViewModel(get(), get(), get(), get(), get()) }
    }
}
