package noctiluca.authentication.domain

import io.ktor.client.engine.*
import kotlinx.serialization.json.Json
import noctiluca.api.authentication.di.AuthenticationApiModule
import noctiluca.api.instancessocial.di.InstancesSocialApiModule
import noctiluca.api.mastodon.di.MastodonApiModule
import noctiluca.api.mastodon.di.buildHttpClient
import noctiluca.authentication.domain.di.AuthenticationDomainModule
import noctiluca.authentication.infra.di.AuthenticationRepositoriesModule
import noctiluca.authentication.infra.repository.local.LocalTokenRepository
import noctiluca.instance.infra.di.InstanceRepositoriesModule
import noctiluca.repository.TokenCache
import noctiluca.test.mock.MockTokenCache
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.newScope
import org.koin.core.module.Module
import org.koin.core.scope.Scope
import org.koin.dsl.koinApplication
import org.koin.dsl.module

class TestAuthenticationUseCaseComponent(
    private val mockHttpClientEngine: HttpClientEngine,
    private val mockLocalTokenRepository: LocalTokenRepository? = null,
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

        single<TokenCache> { MockTokenCache() }

        buildAuthenticationInfraModule()

        InstanceRepositoriesModule()

        scope(scope.scopeQualifier) {
            AuthenticationDomainModule()
        }
    }

    private fun Module.buildAuthenticationInfraModule() {
        mockLocalTokenRepository?.let { repository ->
            single { repository }
        }

        AuthenticationRepositoriesModule()
    }
}
