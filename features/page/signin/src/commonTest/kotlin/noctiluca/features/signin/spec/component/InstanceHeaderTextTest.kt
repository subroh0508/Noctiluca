package noctiluca.features.signin.spec.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import noctiluca.features.signin.INSTANCE_DOMAIN
import noctiluca.features.signin.TestComposable
import noctiluca.features.signin.component.InstancesTab
import noctiluca.features.signin.component.topappbar.InstanceHeaderText
import noctiluca.features.signin.mock.instance
import noctiluca.features.signin.section.scrollableframe.rememberInstanceDetailScrollableFrameState
import noctiluca.test.ui.RunWith
import noctiluca.test.ui.UiTestRunner
import kotlin.test.Test

private const val DUMMY_LAZY_COLUMN = "DummyLazyColumn"

@OptIn(ExperimentalTestApi::class)
@RunWith(UiTestRunner::class)
class InstanceHeaderTextTest {
    @Test
    fun InstanceHeaderText_shouldShowDomainText() = runComposeUiTest {
        setContent {
            val scrollState = rememberInstanceDetailScrollableFrameState(
                InstancesTab.Info,
                listOf(),
            )

            TestComposable {
                InstanceHeaderText(
                    domain = INSTANCE_DOMAIN,
                    instance = null,
                    scrollState = scrollState,
                )
            }
        }

        onNodeWithText(INSTANCE_DOMAIN).assertIsDisplayed()
        onNodeWithText(instance.name).assertDoesNotExist()
    }

    @Test
    fun InstanceHeaderText_shouldShowDomainAndNameText() = runComposeUiTest {
        setContent {
            val scrollState = rememberInstanceDetailScrollableFrameState(
                InstancesTab.Info,
                listOf(),
            )

            TestComposable {
                Box(Modifier.height(100.dp)) {
                    LazyColumn(
                        state = scrollState.lazyListState,
                        modifier = Modifier.fillMaxSize()
                            .testTag(DUMMY_LAZY_COLUMN),
                    ) {
                        items(List(5) { "Item #$it" }) {
                            Text(it, Modifier.height(80.dp))
                        }
                    }

                    InstanceHeaderText(
                        domain = instance.domain,
                        instance = instance,
                        scrollState = scrollState,
                    )
                }
            }
        }

        onNodeWithText(instance.domain).assertDoesNotExist()
        onNodeWithText(instance.name).assertDoesNotExist()

        onNodeWithTag(DUMMY_LAZY_COLUMN).performScrollToNode(hasText("Item #4"))

        onNodeWithText(instance.domain).assertIsDisplayed()
        onNodeWithText(instance.name).assertIsDisplayed()
    }
}
