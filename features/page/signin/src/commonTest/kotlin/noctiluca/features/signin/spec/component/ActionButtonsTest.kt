package noctiluca.features.signin.spec.component

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import io.kotest.matchers.be
import io.kotest.matchers.should
import noctiluca.features.signin.SignInTestTag
import noctiluca.features.signin.TestComposable
import noctiluca.features.signin.component.ActionButtons
import noctiluca.features.signin.mock.instance
import noctiluca.model.authorization.Instance
import noctiluca.test.ui.KmpTest
import noctiluca.test.ui.RunWith
import noctiluca.test.ui.UiTestRunner

@OptIn(ExperimentalTestApi::class)
@RunWith(UiTestRunner::class)
class ActionButtonsTest {
    @KmpTest
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
            .assertDoesNotExist()

        onNodeWithTag(SignInTestTag.AUTHORIZE_BUTTON)
            .assertDoesNotExist()
    }

    @KmpTest
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
            .assertIsDisplayed()

        onNodeWithTag(SignInTestTag.AUTHORIZE_BUTTON)
            .assertDoesNotExist()
    }

    @KmpTest
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
            .assertDoesNotExist()

        with(onNodeWithTag(SignInTestTag.AUTHORIZE_BUTTON)) {
            assertIsDisplayed()
            performClick()
        }

        args should be(instance)
    }
}
