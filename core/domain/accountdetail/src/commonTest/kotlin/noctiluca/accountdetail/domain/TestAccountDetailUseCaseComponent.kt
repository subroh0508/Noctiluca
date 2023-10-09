package noctiluca.accountdetail.domain

import io.ktor.client.engine.*
import kotlinx.serialization.json.Json
import noctiluca.accountdetail.domain.di.AccountDetailDomainModule
import noctiluca.network.mastodon.di.MastodonApiModule
import noctiluca.network.mastodon.di.buildHttpClient
import noctiluca.network.mastodon.di.buildWebSocketClient
import noctiluca.data.di.DataAccountDetailModule
import noctiluca.test.di.MockAccountDataStoreModule
import noctiluca.test.di.MockTokenDataStoreModule
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
        MockAccountDataStoreModule()
        MockTokenDataStoreModule()
        DataAccountDetailModule()

        scope(scope.scopeQualifier) {
            AccountDetailDomainModule()
        }
    }
}
