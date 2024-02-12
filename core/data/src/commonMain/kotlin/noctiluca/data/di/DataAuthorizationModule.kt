package noctiluca.data.di

import noctiluca.data.authorization.AuthorizationRepository
import noctiluca.data.authorization.impl.AuthorizationRepositoryImpl
import org.koin.dsl.ScopeDSL

@Suppress("FunctionName")
fun ScopeDSL.DataAuthorizationModule() {
    scoped<AuthorizationRepository> { AuthorizationRepositoryImpl(get(), get(), get()) }
}
