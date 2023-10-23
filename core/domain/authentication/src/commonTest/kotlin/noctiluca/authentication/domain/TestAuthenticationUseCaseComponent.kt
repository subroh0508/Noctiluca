package noctiluca.authentication.domain

import io.ktor.client.engine.*
import kotlinx.serialization.json.Json
import noctiluca.authentication.domain.di.AuthenticationDomainModule
import noctiluca.data.di.DataAuthenticationModule
import noctiluca.data.di.DataInstanceModule
import noctiluca.datastore.AppCredentialDataStore
import noctiluca.datastore.TokenDataStore
import noctiluca.network.authentication.di.AuthenticationApiModule
import noctiluca.network.instancessocial.di.InstancesSocialApiModule
import noctiluca.network.mastodon.di.MastodonApiModule
import noctiluca.network.mastodon.di.buildHttpClient
import noctiluca.test.di.MockAccountDataStoreModule
import noctiluca.test.di.MockTokenDataStoreModule
import noctiluca.test.di.MockTokenProviderModule
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.newScope
import org.koin.core.scope.Scope
import org.koin.dsl.koinApplication
import org.koin.dsl.module

class TestAuthenticationUseCaseComponent(
    private val mockHttpClientEngine: HttpClientEngine,
    private val appCredentialDataStore: AppCredentialDataStore,
    private val tokenDataStore: TokenDataStore,
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
        AuthenticationApiModule(buildHttpClient(json, mockHttpClientEngine))
        InstancesSocialApiModule(buildHttpClient(json, mockHttpClientEngine), "dummy_token")
        MastodonApiModule(
            buildHttpClient(json, mockHttpClientEngine),
            buildHttpClient(json, mockHttpClientEngine),
            json,
        )

        // MockAccountDataStoreModule()
        // MockTokenDataStoreModule()

        MockTokenProviderModule()
        single { appCredentialDataStore }
        single { tokenDataStore }

        DataAuthenticationModule()
        DataInstanceModule()

        scope(scope.scopeQualifier) {
            AuthenticationDomainModule()
        }
    }
}
