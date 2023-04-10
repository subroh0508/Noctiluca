package noctiluca.accountdetail.domain.di

import noctiluca.accountdetail.domain.usecase.FetchAccountAttributesUseCase
import noctiluca.accountdetail.domain.usecase.internal.FetchAccountAttributesUseCaseImpl
import org.koin.dsl.ScopeDSL

@Suppress("FunctionName")
fun ScopeDSL.AccountDetailDomainModule() {
    scoped<FetchAccountAttributesUseCase> { FetchAccountAttributesUseCaseImpl(get()) }
}
