package noctiluca.components.di

import org.koin.core.context.GlobalContext

fun getKoin() = GlobalContext.get()
fun getKoinOrNull() = GlobalContext.getOrNull()
