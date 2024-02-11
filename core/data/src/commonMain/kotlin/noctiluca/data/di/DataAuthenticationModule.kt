package noctiluca.data.di

import noctiluca.data.authentication.AuthenticationRepository
import noctiluca.data.authentication.impl.AuthenticationRepositoryImpl
import org.koin.dsl.ScopeDSL

@Suppress("FunctionName")
fun ScopeDSL.DataAuthenticationModule() {
    scoped<AuthenticationRepository> { AuthenticationRepositoryImpl(get(), get(), get()) }
}
