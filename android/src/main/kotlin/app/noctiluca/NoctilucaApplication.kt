package app.noctiluca

import android.app.Application
import app.noctiluca.di.AndroidTokenProviderModule
import io.ktor.client.engine.okhttp.*
import kotlinx.serialization.json.Json
import noctiluca.api.mastodon.di.MastodonApiModule
import noctiluca.api.mastodon.di.buildWebSocketClient
import noctiluca.data.di.DataModule
import noctiluca.datastore.di.DataStoreModule
import noctiluca.features.components.di.ImageLoaderModule
import noctiluca.network.authentication.di.AuthenticationApiModule
import noctiluca.network.instancessocial.di.InstancesSocialApiModule
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import noctiluca.network.instancessocial.di.buildHttpClient as buildHttpClientForInstancesSocial
import noctiluca.api.mastodon.di.buildHttpClient as buildHttpClientForMastodon
import noctiluca.network.authentication.di.buildHttpClient as buildHttpClientForAuthentication

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
        DataStoreModule(json)
        AndroidTokenProviderModule()

        AuthenticationApiModule(buildHttpClientForAuthentication(json, httpClientEngine))
        InstancesSocialApiModule(buildHttpClientForInstancesSocial(json, httpClientEngine))
        MastodonApiModule(
            buildHttpClientForMastodon(json, httpClientEngine),
            buildWebSocketClient(httpClientEngine),
            json,
        )
    }

    private fun buildRepositoriesModules() = module {
        DataModule()
    }

    private fun buildFeaturesModules() = ImageLoaderModule(httpClientEngine)
}
