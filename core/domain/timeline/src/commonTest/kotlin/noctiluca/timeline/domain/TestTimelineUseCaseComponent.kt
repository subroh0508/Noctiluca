package noctiluca.timeline.domain

import io.ktor.client.engine.HttpClientEngine
import kotlinx.serialization.json.Json
import noctiluca.network.mastodon.di.MastodonApiModule
import noctiluca.network.mastodon.di.buildHttpClient
import noctiluca.network.mastodon.di.buildWebSocketClient
import noctiluca.data.di.DataAccountModule
import noctiluca.data.di.DataStatusModule
import noctiluca.data.di.DataTimelineModule
import noctiluca.test.di.MockAccountDataStoreModule
import noctiluca.test.di.MockTokenDataStoreModule
import noctiluca.timeline.domain.di.TimelineDomainModule
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.newScope
import org.koin.core.scope.Scope
import org.koin.dsl.koinApplication
import org.koin.dsl.module

class TestTimelineUseCaseComponent(
    private val mockHttpClientEngine: HttpClientEngine,
) : KoinScopeComponent {
    private val json by lazy {
        Json {
            explicitNulls = false
            encodeDefaults = true
            ignoreUnknownKeys = true
        }
    }

    private val koinApplication = koinApplication()

    override fun getKoin() = koinApplication.koin
    override val scope: Scope by newScope()

    init {
        getKoin().loadModules(listOf(buildModule()))
    }

    private fun buildModule() = module {
        MastodonApiModule(
            buildHttpClient(json, mockHttpClientEngine),
            buildWebSocketClient(mockHttpClientEngine),
            json,
        )
        MockAccountDataStoreModule()
        MockTokenDataStoreModule()

        DataAccountModule()
        DataStatusModule()
        DataTimelineModule()

        scope(scope.scopeQualifier) {
            TimelineDomainModule()
        }
    }
}
