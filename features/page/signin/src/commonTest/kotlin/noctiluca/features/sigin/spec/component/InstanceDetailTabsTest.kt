package noctiluca.features.sigin.spec.component

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.kotest.matchers.be
import io.kotest.matchers.should
import noctiluca.features.sigin.TestComposable
import noctiluca.features.signin.component.InstanceDetailTabs
import noctiluca.features.signin.component.InstancesTab
import noctiluca.features.signin.section.scrollableframe.rememberInstanceDetailScrollableFrameState
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class InstanceDetailTabsTest {
    @Test
    fun InstanceDetailTabs_shouldNotDisplay() = runComposeUiTest {
        setContent {
            val scrollState = rememberInstanceDetailScrollableFrameState(
                InstancesTab.Info,
                listOf(),
            )

            TestComposable {
                InstanceDetailTabs(
                    current = InstancesTab.Info,
                    tabList = listOf(),
                    scrollState = scrollState,
                    onSwitch = {},
                )
            }
        }

        InstancesTab.entries.forEach {
            onNodeWithTag(it.name).assertDoesNotExist()
        }
    }

    @Test
    fun InstanceDetailTabs_shouldInvokeCallbackCorrectly() = runComposeUiTest {
        var tab: InstancesTab = InstancesTab.entries.last()

        setContent {
            val scrollState = rememberInstanceDetailScrollableFrameState(
                tab,
                InstancesTab.entries
            )

            TestComposable {
                InstanceDetailTabs(
                    current = tab,
                    tabList = InstancesTab.entries,
                    scrollState = scrollState,
                    onSwitch = { tab = it },
                )
            }
        }

        InstancesTab.entries.forEach {
            onNodeWithTag(it.name).isDisplayed()
            onNodeWithTag(it.name).performClick()

            tab should be(it)
        }
    }
}
