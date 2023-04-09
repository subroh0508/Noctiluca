package noctiluca.accountdetail.domain

import io.ktor.client.engine.*
import kotlinx.serialization.json.Json
import noctiluca.accountdetail.domain.di.AccountDetailDomainModule
import noctiluca.accountdetail.infra.di.AccountDetailRepositoriesModule
import noctiluca.api.mastodon.di.MastodonApiModule
import noctiluca.api.mastodon.di.buildHttpClient
import noctiluca.api.mastodon.di.buildWebSocketClient
import noctiluca.test.di.MockTokenModule
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.newScope
import org.koin.core.scope.Scope
import org.koin.dsl.koinApplication
import org.koin.dsl.module

class TestAccountDetailUseCaseComponent(
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
        MockTokenModule()

        AccountDetailRepositoriesModule()

        scope(scope.scopeQualifier) {
            AccountDetailDomainModule()
        }
    }
}
