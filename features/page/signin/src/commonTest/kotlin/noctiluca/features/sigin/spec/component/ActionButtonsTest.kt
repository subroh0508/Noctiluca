package noctiluca.features.sigin.spec.component

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import io.kotest.matchers.be
import io.kotest.matchers.should
import noctiluca.features.sigin.TestComposable
import noctiluca.features.signin.SignInTestTag
import noctiluca.features.signin.component.ActionButtons
import noctiluca.model.Uri
import noctiluca.model.authorization.Instance
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ActionButtonsTest {
    private val instance = Instance(
        name = "mastodon.social",
        domain = "mstdn.social",
        description = "A general-purpose Mastodon server with a 500 character limit. All languages are welcome.",
        thumbnail = Uri("https://media.mstdn.social/site_uploads/thumbnail.png"),
        languages = listOf("ja"),
        activeUserCount = 10_000,
        administrator = Instance.Administrator(
            screen = "@stux@mstdn.social",
            displayName = "stux⚡",
            url = Uri("https://mstdn.social/@stux"),
            avatar = Uri("https://media.mstdn.social/accounts/avatars/avatar.png"),
        ),
        rules = listOf(
            Instance.Rule(
                "aaa",
                "Sexually explicit or violent media must be marked as sensitive when posting."
            ),
            Instance.Rule("bbb", "No spam or advertising."),
        ),
        extendedDescription = "A general-purpose Mastodon server with a 500 character limit. All languages are welcome.",
        version = Instance.Version(4, 2, 8),
    )

    @Test
    fun AuthorizeButton_shouldNotDisplay() = runComposeUiTest {
        setContent {
            TestComposable {
                Box {
                    ActionButtons(
                        instance = null,
                        isSignInProgress = false,
                        horizontalPadding = 0.dp,
                        onClickAuthorize = {},
                    )
                }
            }
        }

        onNodeWithTag(SignInTestTag.AUTHORIZE_BUTTON_PROGRESS_CIRCLE)
            .isNotDisplayed()

        onNodeWithTag(SignInTestTag.AUTHORIZE_BUTTON)
            .isNotDisplayed()
    }

    @Test
    fun AuthorizeButton_shouldShowCircularProgressIndicator() = runComposeUiTest {
        setContent {
            TestComposable {
                Box {
                    ActionButtons(
                        instance = instance,
                        isSignInProgress = true,
                        horizontalPadding = 0.dp,
                        onClickAuthorize = {},
                    )
                }
            }
        }

        onNodeWithTag(SignInTestTag.AUTHORIZE_BUTTON_PROGRESS_CIRCLE)
            .isDisplayed()

        onNodeWithTag(SignInTestTag.AUTHORIZE_BUTTON)
            .isNotDisplayed()
    }

    @Test
    fun AuthorizeButton_shouldInvokeCallbackCorrectly() = runComposeUiTest {
        var args: Instance? = null

        setContent {
            TestComposable {
                Box {
                    ActionButtons(
                        instance,
                        isSignInProgress = false,
                        horizontalPadding = 0.dp,
                        onClickAuthorize = { args = it },
                    )
                }
            }
        }

        onNodeWithTag(SignInTestTag.AUTHORIZE_BUTTON_PROGRESS_CIRCLE)
            .isNotDisplayed()

        with(onNodeWithTag(SignInTestTag.AUTHORIZE_BUTTON)) {
            isDisplayed()
            performClick()
        }

        args should be(instance)
    }
}
