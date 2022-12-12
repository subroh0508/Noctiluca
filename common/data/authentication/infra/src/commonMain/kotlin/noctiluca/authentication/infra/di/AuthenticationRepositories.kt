package noctiluca.authentication.infra.di

import noctiluca.authentication.infra.repository.TokenRepository
import noctiluca.authentication.infra.repository.impl.TokenRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module

expect object AuthenticationRepositories {
    val Module: Module
}
