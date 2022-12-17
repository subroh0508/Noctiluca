package noctiluca.authentication.domain.di

import noctiluca.authentication.domain.usecase.SearchMastodonInstancesUseCase
import noctiluca.authentication.domain.usecase.internal.SearchMastodonInstancesUseCaseImpl
import org.koin.dsl.module

object AuthenticationDomainModule {
    operator fun invoke() = module {
        single<SearchMastodonInstancesUseCase> { SearchMastodonInstancesUseCaseImpl(get()) }
    }
}
