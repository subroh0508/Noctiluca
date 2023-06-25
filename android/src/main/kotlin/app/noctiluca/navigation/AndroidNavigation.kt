package app.noctiluca.navigation

import android.content.Context
import androidx.navigation.NavHostController
import noctiluca.features.authentication.SignInNavigation
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.components.Navigation
import noctiluca.features.components.utils.Browser
import noctiluca.features.timeline.TimelineNavigation
import noctiluca.model.Uri
import android.net.Uri as AndroidUri

class AndroidNavigation private constructor(
    val navHostController: NavHostController,
    private val browser: Browser,
) : Navigation, SignInNavigation, TimelineNavigation {
    constructor(
        navHostController: NavHostController,
        context: Context,
    ) : this(
        navHostController,
        Browser(context),
    )

    override fun backPressed() {
        navHostController.popBackStack()
    }

    override fun openBrowser(uri: Uri) = browser.open(uri)

    override fun reopenApp() {
        navHostController.navigate(RouteTimelines) {
            popUpTo(RouteTimelines) { inclusive = true }
        }
    }

    override fun backToSignIn() {
        navHostController.navigate(RouteSignIn) {
            popUpTo(RouteTimelines) { inclusive = true }
        }
    }

    override fun navigateToTimelines() {
        navHostController.navigate(RouteTimelines) {
            popUpTo(RouteSignIn) { inclusive = true }
        }
    }

    override fun navigateToInstanceDetail(domain: String) {
        navHostController.navigate("$ComposableInstanceDetail?${AuthorizeResult.QUERY_DOMAIN}=$domain")
    }

    override fun navigateToAccountDetail(id: String) {
        navHostController.navigate("$RouteAccountDetail/$id")
    }

    override fun navigateToToot() {
        navHostController.navigate(RouteToot)
    }

    fun redirectToSignIn(uri: AndroidUri?) {
        uri ?: return

        navHostController.navigate(
            buildString {
                append("$ComposableInstanceDetail?")
                append("${AuthorizeResult.QUERY_DOMAIN}=${uri.host}&")
                append(uri.query)
            },
        ) {
            launchSingleTop = true
        }
    }
}
