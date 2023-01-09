package noctiluca.features.timeline.di

import noctiluca.features.components.di.FeatureComponent
import noctiluca.timeline.domain.di.TimelineDomainModule
import org.koin.dsl.module

class TimelineComponent : FeatureComponent({ scope ->
    module {
        scope(scope.scopeQualifier) {
            TimelineDomainModule()
        }
    }
})
