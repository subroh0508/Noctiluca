package noctiluca.accountdetail.domain.di

import noctiluca.accountdetail.domain.usecase.FetchAccountAttributesUseCase
import noctiluca.accountdetail.domain.usecase.FetchAccountStatusesUseCase
import noctiluca.accountdetail.domain.usecase.internal.FetchAccountAttributesUseCaseImpl
import noctiluca.accountdetail.domain.usecase.internal.FetchAccountStatusesUseCaseImpl
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.AccountDetailDomainModule() {
    single<FetchAccountAttributesUseCase> { FetchAccountAttributesUseCaseImpl(get()) }
    single<FetchAccountStatusesUseCase> { FetchAccountStatusesUseCaseImpl(get()) }
}
