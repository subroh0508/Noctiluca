package noctiluca.data

import io.ktor.client.engine.HttpClientEngine
import kotlinx.serialization.json.Json
import noctiluca.datastore.AuthorizationTokenDataStore
import noctiluca.network.mastodon.AuthorizationTokenProvider
import noctiluca.network.mastodon.di.MastodonApiModule
import noctiluca.network.mastodon.di.buildHttpClient
import noctiluca.network.mastodon.di.buildWebSocketClient
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.newScope
import org.koin.core.scope.Scope
import org.koin.dsl.ModuleDeclaration
import org.koin.dsl.koinApplication
import org.koin.dsl.module

class TestDataComponent(
    private val mockHttpClientEngine: HttpClientEngine,
    private val mockAuthorizationTokenDataStore: AuthorizationTokenDataStore,
    private val moduleDeclaration: ModuleDeclaration = {},
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

        single<AuthorizationTokenProvider> {
            object : AuthorizationTokenProvider {
                override suspend fun getCurrentAccessToken() = mockAuthorizationTokenDataStore.getCurrentAccessToken()
                override suspend fun getCurrentDomain() = mockAuthorizationTokenDataStore.getCurrentDomain()
            }
        }
        single { mockAuthorizationTokenDataStore }

        moduleDeclaration()
    }
}
