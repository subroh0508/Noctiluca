package app.noctiluca

import android.app.Application
import io.ktor.client.engine.okhttp.*
import kotlinx.serialization.json.Json
import noctiluca.api.authentication.di.buildHttpClient as buildHttpClientForAuthentication
import noctiluca.api.instancessocial.di.buildHttpClient as buildHttpClientForInstancesSocial
import noctiluca.api.instancessocial.di.buildHttpClient as buildHttpClientForMastodon
import noctiluca.api.authentication.di.AuthenticationApiModule
import noctiluca.api.instancessocial.di.InstancesSocialApiModule
import noctiluca.api.mastodon.MastodonApi
import noctiluca.api.mastodon.di.MastodonApiModule
import noctiluca.api.token.di.TokenApiModule
import noctiluca.authentication.infra.di.AuthenticationRepositoriesModule
import noctiluca.components.di.ImageLoaderModule
import noctiluca.features.authentication.di.SignInModule
import noctiluca.instance.infra.di.InstanceRepositoriesModule
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class NoctilucaApplication : Application() {
    private val json by lazy {
        Json {
            explicitNulls = false
            encodeDefaults = true
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
            modules(buildApiModules() + buildRepositoriesModules() + buildFeaturesModules())
        }
    }

    private fun buildApiModules() = module {
        AuthenticationApiModule(buildHttpClientForAuthentication(json, httpClientEngine))
        InstancesSocialApiModule(buildHttpClientForInstancesSocial(json, httpClientEngine))
        MastodonApiModule(buildHttpClientForMastodon(json, httpClientEngine))
        TokenApiModule()
    }

    private fun buildRepositoriesModules() = module {
        AuthenticationRepositoriesModule()
        InstanceRepositoriesModule()
    }

    private fun buildFeaturesModules() = ImageLoaderModule(httpClientEngine) +
            SignInModule()
}