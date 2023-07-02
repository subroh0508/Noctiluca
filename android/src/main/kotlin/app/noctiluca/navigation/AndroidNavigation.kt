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
        navHostController.navigate(RouteTimeline) {
            popUpTo(RouteTimeline) { inclusive = true }
        }
    }

    override fun backToSignIn() {
        navHostController.navigate(RouteSignIn) {
            popUpTo(RouteTimeline) { inclusive = true }
        }
    }

    override fun navigateToTimelines() {
        navHostController.navigate(RouteTimeline) {
            popUpTo(RouteSignIn) { inclusive = true }
        }
    }

    override fun navigateToAccountDetail(id: String) {
        navHostController.navigate("$RouteAccountDetail/$id")
    }

    override fun navigateToToot() {
        navHostController.navigate(ComposableToot)
    }

    fun redirectToSignIn(uri: AndroidUri?) {
        uri ?: return

        navHostController.navigate(
            buildString {
                append("$RouteSignIn?")
                append("${AuthorizeResult.QUERY_DOMAIN}=${uri.host}&")
                append(uri.query)
            },
        ) {
            launchSingleTop = true
        }
    }
}
