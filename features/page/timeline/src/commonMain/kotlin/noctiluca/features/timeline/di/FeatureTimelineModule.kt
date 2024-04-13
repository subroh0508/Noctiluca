package noctiluca.features.timeline.di

import noctiluca.data.di.AuthorizedContext
import noctiluca.features.timeline.TimelinesScreen
import noctiluca.features.timeline.viewmodel.AuthorizedAccountViewModel
import noctiluca.features.timeline.viewmodel.TimelinesViewModel
import noctiluca.features.toot.viewmodel.TootViewModel
import noctiluca.timeline.domain.di.TimelineDomainModule
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.FeatureTimelineModule() {
    scope<AuthorizedContext> {
        TimelineDomainModule()
    }

    scope<TimelinesScreen> {
        scoped { AuthorizedAccountViewModel(get(), get()) }
        scoped { TimelinesViewModel(get(), get(), get(), get(), get()) }
        scoped { TootViewModel(get(), get()) }
    }
}
