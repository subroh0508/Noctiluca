package noctiluca.data.di

import noctiluca.data.authentication.AuthorizationRepository
import noctiluca.data.authentication.impl.AuthorizationRepositoryImpl
import org.koin.dsl.ScopeDSL

@Suppress("FunctionName")
fun ScopeDSL.DataAuthenticationModule() {
    scoped<AuthorizationRepository> { AuthorizationRepositoryImpl(get(), get(), get()) }
}
