package app.noctiluca

import android.app.Application
import io.ktor.client.engine.okhttp.*
import kotlinx.serialization.json.Json
import noctiluca.api.authentication.di.buildHttpClient as buildHttpClientForAuthentication
import noctiluca.api.instancessocial.di.buildHttpClient as buildHttpClientForInstancesSocial
import noctiluca.api.authentication.di.AuthenticationApiModule
import noctiluca.api.instancessocial.di.InstancesSocialApiModule
import noctiluca.authentication.infra.di.AuthenticationRepositoriesModule
import noctiluca.instance.infra.di.InstanceRepositoriesModule
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class NoctilucaApplication : Application() {
    private val json by lazy {
        Json {
            explicitNulls = false
            ignoreUnknownKeys = true
        }
    }

    private val httpClientEngine by lazy {
        OkHttp.create {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            addInterceptor(loggingInterceptor)
        }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            // androidLogger(Level.DEBUG)
            androidContext(this@NoctilucaApplication)
            modules(buildApiModules() + buildRepositoriesModules())
        }
    }

    private fun buildApiModules() = InstancesSocialApiModule(
        client = buildHttpClientForInstancesSocial(json, httpClientEngine),
    ) + AuthenticationApiModule(
        client = buildHttpClientForAuthentication(json, httpClientEngine),
    )

    private fun buildRepositoriesModules() = AuthenticationRepositoriesModule() +
            InstanceRepositoriesModule()
}