package app.noctiluca

import android.app.Application
import app.noctiluca.di.AndroidAuthenticationTokenProviderModule
import io.ktor.client.engine.okhttp.*
import kotlinx.serialization.json.Json
import noctiluca.data.di.DataModule
import noctiluca.datastore.di.DataStoreModule
import noctiluca.features.components.di.ImageLoaderModule
import noctiluca.network.authentication.di.AuthenticationApiModule
import noctiluca.network.instancessocial.di.InstancesSocialApiModule
import noctiluca.network.mastodon.di.MastodonApiModule
import noctiluca.network.mastodon.di.buildWebSocketClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import noctiluca.network.authentication.di.buildHttpClient as buildHttpClientForAuthentication
import noctiluca.network.instancessocial.di.buildHttpClient as buildHttpClientForInstancesSocial
import noctiluca.network.mastodon.di.buildHttpClient as buildHttpClientForMastodon

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
        AndroidAuthenticationTokenProviderModule()

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
