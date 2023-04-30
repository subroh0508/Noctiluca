package noctiluca.accountdetail.domain.di

import noctiluca.accountdetail.domain.usecase.FetchAccountAttributesUseCase
import noctiluca.accountdetail.domain.usecase.FetchAccountStatusesUseCase
import noctiluca.accountdetail.domain.usecase.internal.FetchAccountAttributesUseCaseImpl
import noctiluca.accountdetail.domain.usecase.internal.FetchAccountStatusesUseCaseImpl
import org.koin.dsl.ScopeDSL

@Suppress("FunctionName")
fun ScopeDSL.AccountDetailDomainModule() {
    scoped<FetchAccountAttributesUseCase> { FetchAccountAttributesUseCaseImpl(get()) }
    scoped<FetchAccountStatusesUseCase> { FetchAccountStatusesUseCaseImpl(get()) }
}
