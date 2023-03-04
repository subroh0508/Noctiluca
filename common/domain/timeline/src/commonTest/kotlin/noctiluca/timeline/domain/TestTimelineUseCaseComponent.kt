package noctiluca.timeline.domain

import io.ktor.client.engine.HttpClientEngine
import kotlinx.serialization.json.Json
import noctiluca.account.infra.di.AccountRepositoriesModule
import noctiluca.api.mastodon.di.MastodonApiModule
import noctiluca.api.mastodon.di.buildHttpClient
import noctiluca.api.mastodon.di.buildWebSocketClient
import noctiluca.status.infra.di.StatusRepositoriesModule
import noctiluca.test.di.MockTokenModule
import noctiluca.timeline.domain.di.TimelineDomainModule
import noctiluca.timeline.infra.di.TimelineRepositoriesModule
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.newScope
import org.koin.core.context.startKoin
import org.koin.core.scope.Scope
import org.koin.dsl.module

class TestTimelineUseCaseComponent(
    private val mockHttpClientEngine: HttpClientEngine,
) : KoinScopeComponent {
    override val scope: Scope by newScope()

    private val json by lazy {
        Json {
            explicitNulls = false
            encodeDefaults = true
            ignoreUnknownKeys = true
        }
    }

    init { startKoin { modules(buildModule()) } }

    private fun buildModule() = module {
        MastodonApiModule(
            buildHttpClient(json, mockHttpClientEngine),
            buildWebSocketClient(mockHttpClientEngine),
            json,
        )
        MockTokenModule()

        AccountRepositoriesModule(json)
        StatusRepositoriesModule()
        TimelineRepositoriesModule()

        scope(scope.scopeQualifier) {
            TimelineDomainModule()
        }
    }
}
