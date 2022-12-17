package noctiluca.authentication.infra.di

import org.koin.core.module.Module

expect object AuthenticationRepositoriesModule {
    operator fun invoke(): Module
}
