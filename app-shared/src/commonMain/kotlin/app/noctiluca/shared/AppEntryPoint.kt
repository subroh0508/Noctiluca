package app.noctiluca.shared

import app.noctiluca.shared.di.AuthorizationTokenProviderModule
import cafe.adriel.voyager.core.registry.ScreenRegistry
import com.seiko.imageloader.ImageLoader
import io.ktor.client.engine.HttpClientEngine
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import noctiluca.data.di.DataModule
import noctiluca.datastore.di.DataStoreModule
import noctiluca.features.accountdetail.di.FeatureAccountDetailModule
import noctiluca.features.accountdetail.featureAccountDetailScreenModule
import noctiluca.features.attachment.featureAttachmentScreenModule
import noctiluca.features.shared.di.AuthorizedFeatureModule
import noctiluca.features.signin.di.FeatureSignInModule
import noctiluca.features.signin.featureSignInScreenModule
import noctiluca.features.statusdetail.di.FeatureStatusDetailModule
import noctiluca.features.statusdetail.featureStatusDetailScreenModule
import noctiluca.features.timeline.di.FeatureTimelineModule
import noctiluca.features.timeline.featureTimelineScreenModule
import noctiluca.features.toot.di.FeatureTootModule
import noctiluca.features.toot.featureTootScreenModule
import noctiluca.network.authorization.di.AuthorizationApiModule
import noctiluca.network.instancessocial.di.InstancesSocialApiModule
import noctiluca.network.mastodon.di.MastodonApiModule
import noctiluca.network.mastodon.di.buildWebSocketClient
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module
import noctiluca.network.authorization.di.buildHttpClient as buildHttpClientForAuthentication
import noctiluca.network.instancessocial.di.buildHttpClient as buildHttpClientForInstancesSocial
import noctiluca.network.mastodon.di.buildHttpClient as buildHttpClientForMastodon

object AppEntryPoint {
    @OptIn(ExperimentalSerializationApi::class)
    private val json by lazy {
        Json {
            explicitNulls = false
            encodeDefaults = true
            ignoreUnknownKeys = true
        }
    }
    private var koinApplication: KoinApplication? = null

    fun init(
        httpClientEngine: HttpClientEngine,
        imageLoader: ImageLoader,
        block: KoinApplication.() -> Unit = {},
    ): KoinApplication {
        registerScreen()

        val app = koinApplication ?: startKoin {
            block()

            modules(
                buildApiModules(httpClientEngine),
                DataModule(),
                buildFeaturesModule(),
                module { single { imageLoader } },
            )
        }
        koinApplication = app

        return app
    }

    private fun registerScreen() = ScreenRegistry {
        featureAccountDetailScreenModule()
        featureAttachmentScreenModule()
        featureSignInScreenModule()
        featureStatusDetailScreenModule()
        featureTimelineScreenModule()
        featureTootScreenModule()
    }

    private fun buildApiModules(
        httpClientEngine: HttpClientEngine,
    ) = module {
        DataStoreModule(json)
        AuthorizationTokenProviderModule()

        AuthorizationApiModule(buildHttpClientForAuthentication(json, httpClientEngine))
        InstancesSocialApiModule(buildHttpClientForInstancesSocial(json, httpClientEngine))
        MastodonApiModule(
            buildHttpClientForMastodon(json, httpClientEngine),
            buildWebSocketClient(httpClientEngine),
            json,
        )
    }

    private fun buildFeaturesModule() = module {
        AuthorizedFeatureModule()
        FeatureSignInModule()
        FeatureTimelineModule()
        FeatureTootModule()
        FeatureAccountDetailModule()
        FeatureStatusDetailModule()
    }
}
