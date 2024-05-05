package noctiluca.features.signin.spec.component

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.kotest.matchers.be
import io.kotest.matchers.should
import noctiluca.features.signin.TestComposable
import noctiluca.features.signin.component.InstanceDetailTabs
import noctiluca.features.signin.component.InstancesTab
import noctiluca.features.signin.section.scrollableframe.rememberInstanceDetailScrollableFrameState
import noctiluca.test.ui.KmpTest
import noctiluca.test.ui.RunWith
import noctiluca.test.ui.UiTestRunner

@OptIn(ExperimentalTestApi::class)
@RunWith(UiTestRunner::class)
class InstanceDetailTabsTest {
    @KmpTest
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

    @KmpTest
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
            onNodeWithTag(it.name).assertIsDisplayed()
            onNodeWithTag(it.name).performClick()

            tab should be(it)
        }
    }
}
