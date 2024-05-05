package noctiluca.features.signin.spec.component

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import io.kotest.matchers.be
import io.kotest.matchers.should
import noctiluca.features.signin.INSTANCE_DOMAIN
import noctiluca.features.signin.Resources
import noctiluca.features.signin.SignInTestTag
import noctiluca.features.signin.TestComposable
import noctiluca.features.signin.component.DEBOUNCE_TIME_MILLIS
import noctiluca.features.signin.component.QueryTextField
import noctiluca.test.ui.KmpIgnore
import noctiluca.test.ui.KmpTest
import noctiluca.test.ui.RunWith
import noctiluca.test.ui.UiTestRunner

@OptIn(ExperimentalTestApi::class)
@RunWith(UiTestRunner::class)
class QueryTextFieldTest {
    private val stringRes = Resources("ja").getString()

    @KmpTest
    fun QueryTextField_shouldShowEmptyText() = runComposeUiTest {
        setContent {
            TestComposable {
                QueryTextField(
                    initQuery = "",
                    onDebouncedTextChange = {},
                )
            }
        }

        onNodeWithTag(SignInTestTag.QUERY_TEXT_FIELD)
            .assertTextEquals("", stringRes.sign_in_search_instance_hint)
        onNodeWithTag(SignInTestTag.QUERY_TEXT_FIELD_CLEAR_ICON)
            .assertDoesNotExist()
    }

    @KmpTest
    @KmpIgnore
    fun QueryTextField_shouldInvokeDebouncedCallbackCorrectly() = runComposeUiTest {
        var query = ""

        setContent {
            TestComposable {
                QueryTextField(
                    initQuery = query,
                    onDebouncedTextChange = { query = it },
                )
            }
        }

        mainClock.autoAdvance = false
        with(onNodeWithTag(SignInTestTag.QUERY_TEXT_FIELD)) {
            performTextInput(INSTANCE_DOMAIN)
            mainClock.advanceTimeByFrame()
            assertTextEquals(INSTANCE_DOMAIN)
        }

        onNodeWithTag(SignInTestTag.QUERY_TEXT_FIELD_CLEAR_ICON)
            .assertIsDisplayed()

        mainClock.advanceTimeBy(DEBOUNCE_TIME_MILLIS)

        query should be(INSTANCE_DOMAIN)
    }
}
