package noctiluca.accountdetail.domain.di

import noctiluca.accountdetail.domain.usecase.FetchAccountDetailUseCase
import noctiluca.accountdetail.domain.usecase.internal.FetchAccountDetailUseCaseImpl
import org.koin.dsl.ScopeDSL

@Suppress("FunctionName")
fun ScopeDSL.AccountDetailDomainModule() {
    scoped<FetchAccountDetailUseCase> { FetchAccountDetailUseCaseImpl(get()) }
}
