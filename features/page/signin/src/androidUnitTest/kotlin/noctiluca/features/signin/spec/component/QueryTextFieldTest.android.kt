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
import noctiluca.features.signin.SignInTestTag
import noctiluca.features.signin.TestComposable
import noctiluca.features.signin.component.DEBOUNCE_TIME_MILLIS
import noctiluca.features.signin.component.QueryTextField
import noctiluca.test.ui.ComposeTest
import noctiluca.test.ui.RunWith
import noctiluca.test.ui.UiTestRunner

@OptIn(ExperimentalTestApi::class)
@RunWith(UiTestRunner::class)
class AndroidQueryTextFieldTest {
    @ComposeTest
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

        with(onNodeWithTag(SignInTestTag.QUERY_TEXT_FIELD)) {
            performTextInput(INSTANCE_DOMAIN)
            assertTextEquals(INSTANCE_DOMAIN)
        }

        onNodeWithTag(SignInTestTag.QUERY_TEXT_FIELD_CLEAR_ICON)
            .assertIsDisplayed()

        mainClock.advanceTimeBy(DEBOUNCE_TIME_MILLIS)

        query should be(INSTANCE_DOMAIN)
    }
}
