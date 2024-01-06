package noctiluca.accountdetail.domain.di

import noctiluca.accountdetail.domain.usecase.FetchAccountStatusesUseCase
import noctiluca.accountdetail.domain.usecase.internal.FetchAccountStatusesUseCaseImpl
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.AccountDetailDomainModule() {
    single<FetchAccountStatusesUseCase> { FetchAccountStatusesUseCaseImpl(get()) }
}
