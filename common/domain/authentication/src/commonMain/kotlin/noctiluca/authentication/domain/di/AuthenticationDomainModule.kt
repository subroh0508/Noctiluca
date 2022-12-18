package noctiluca.authentication.domain.di

import noctiluca.authentication.domain.usecase.SearchMastodonInstancesUseCase
import noctiluca.authentication.domain.usecase.internal.SearchMastodonInstancesUseCaseImpl
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module

object AuthenticationDomainModule {
    operator fun invoke(qualifier: Qualifier) = module {
        scope(qualifier) {
            scoped<SearchMastodonInstancesUseCase> { SearchMastodonInstancesUseCaseImpl(get()) }
        }
    }
}
