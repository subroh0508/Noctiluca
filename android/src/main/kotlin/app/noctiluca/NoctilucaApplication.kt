package app.noctiluca

import android.app.Application
import noctiluca.api.authentication.di.AuthenticationApiModule
import noctiluca.api.instancessocial.di.InstancesSocialApiModule
import noctiluca.authentication.infra.di.AuthenticationRepositoriesModule
import noctiluca.instance.infra.di.InstanceRepositoriesModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NoctilucaApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@NoctilucaApplication)
            modules(buildApiModules() + buildRepositoriesModules())
        }
    }

    private fun buildApiModules() = AuthenticationApiModule() +
            InstancesSocialApiModule()
    private fun buildRepositoriesModules() = AuthenticationRepositoriesModule() +
            InstanceRepositoriesModule()
}