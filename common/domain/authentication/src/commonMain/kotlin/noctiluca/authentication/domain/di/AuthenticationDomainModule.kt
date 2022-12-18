package noctiluca.authentication.domain.di

import noctiluca.authentication.domain.usecase.SearchMastodonInstancesUseCase
import noctiluca.authentication.domain.usecase.internal.SearchMastodonInstancesUseCaseImpl
import org.koin.dsl.ScopeDSL

@Suppress("FunctionName")
fun ScopeDSL.AuthenticationDomainModule() {
    scoped<SearchMastodonInstancesUseCase> { SearchMastodonInstancesUseCaseImpl(get()) }
}
