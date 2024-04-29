package noctiluca.features.sigin.spec.component

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import io.kotest.matchers.be
import io.kotest.matchers.should
import noctiluca.features.sigin.INSTANCE_DOMAIN
import noctiluca.features.sigin.TestComposable
import noctiluca.features.signin.Resources
import noctiluca.features.signin.SignInTestTag
import noctiluca.features.signin.component.DEBOUNCE_TIME_MILLIS
import noctiluca.features.signin.component.QueryTextField
import kotlin.test.Ignore
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class QueryTextFieldTest {
    private val stringRes = Resources("ja").getString()

    @Test
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

    @Test
    @Ignore
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
