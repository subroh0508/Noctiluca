package app.noctiluca.navigation

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import app.noctiluca.decompose.DefaultRootComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import noctiluca.features.accountdetail.AccountDetailNavigator
import noctiluca.features.authentication.SignInNavigator
import noctiluca.features.components.Navigator
import noctiluca.features.components.utils.Browser
import noctiluca.features.timeline.TimelineNavigator
import noctiluca.model.AccountId
import noctiluca.model.Uri
import android.net.Uri as AndroidUri
import org.koin.core.scope.Scope

class AndroidNavigator(
    private val rootComponentContext: ComponentContext,
    private val browser: Browser,
) : Navigator,
    SignInNavigator,
    TimelineNavigator,
    AccountDetailNavigator,
    ComponentContext by rootComponentContext {

    constructor(activity: AppCompatActivity) : this(
        DefaultRootComponent(activity),
        Browser(activity),
    )

    private val navigation by lazy { StackNavigation<Config>() }

    val childStack: Value<ChildStack<*, Feature>> = childStack(
        source = navigation,
        initialConfiguration = Config.Timeline,
        handleBackButton = true,
        childFactory = { config, _ ->
            when (config) {
                is Config.SignIn -> Feature.SignIn(config.domain, config.query)
                is Config.Timeline -> Feature.Timeline
                is Config.AccountDetail -> Feature.AccountDetail(AccountId(config.id))
            }
        }
    )

    override fun backPressed() {
        navigation.pop()
    }

    override fun openBrowser(uri: Uri) {
        browser.open(uri)
    }

    override fun reopenApp() {
        navigation.navigate { listOf(Config.Timeline) }
    }

    override fun backToSignIn() {
        navigation.navigate { listOf(Config.SignIn()) }
    }

    override fun navigateToTimelines() {
        navigation.navigate { listOf(Config.Timeline) }
    }

    override fun navigateToAccountDetail(id: String) {
        navigation.push(Config.AccountDetail(id))
    }

    fun redirectToSignIn(uri: AndroidUri) {
        if (childStack.value.active.instance !is Feature.SignIn) {
            return
        }

        navigation.replaceCurrent(Config.SignIn(uri.host, uri.query))
    }

    sealed class Feature : Navigator.Destination {
        class SignIn(
            val domain: String? = null,
            val query: String? = null,
        ) : Feature()

        object Timeline : Feature()
        class AccountDetail(val id: AccountId) : Feature()
    }

    private sealed class Config : Navigator.Config, Parcelable {
        @Parcelize
        data class SignIn(
            val domain: String? = null,
            val query: String? = null,
        ) : Config()

        @Parcelize
        object Timeline : Config()

        @Parcelize
        data class AccountDetail(val id: String) : Config()
    }
}
