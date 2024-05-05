package noctiluca.features.signin.spec.component

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import noctiluca.features.signin.HTML_INSTANCE_DESCRIPTION
import noctiluca.features.signin.INSTANCE_DESCRIPTION
import noctiluca.features.signin.INSTANCE_DOMAIN
import noctiluca.features.signin.INSTANCE_NAME
import noctiluca.features.signin.component.InstanceDescription
import noctiluca.features.signin.component.InstanceName
import noctiluca.test.ui.KmpTest
import noctiluca.test.ui.RunWith
import noctiluca.test.ui.UiTestRunner

@OptIn(ExperimentalTestApi::class)
@RunWith(UiTestRunner::class)
class CaptionTest {
    @KmpTest
    fun InstanceName_shouldShowNameAndDomain() = runComposeUiTest {
        setContent {
            InstanceName(
                name = INSTANCE_NAME,
                domain = INSTANCE_DOMAIN,
                horizontalPadding = 0.dp,
            )
        }

        onNodeWithText(INSTANCE_NAME).assertIsDisplayed()
        onNodeWithText(INSTANCE_DOMAIN).assertIsDisplayed()
    }

    @KmpTest
    fun InstanceDescription_shouldShowNameAndDomain() = runComposeUiTest {
        setContent {
            InstanceDescription(
                description = HTML_INSTANCE_DESCRIPTION,
                horizontalPadding = 0.dp,
            )
        }

        onNodeWithText(INSTANCE_DESCRIPTION + "\n").assertIsDisplayed()
    }
}
