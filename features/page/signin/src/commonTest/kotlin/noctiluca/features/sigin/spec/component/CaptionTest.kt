package noctiluca.features.sigin.spec.component

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import noctiluca.features.sigin.HTML_INSTANCE_DESCRIPTION
import noctiluca.features.sigin.INSTANCE_DESCRIPTION
import noctiluca.features.sigin.INSTANCE_DOMAIN
import noctiluca.features.sigin.INSTANCE_NAME
import noctiluca.features.signin.component.InstanceDescription
import noctiluca.features.signin.component.InstanceName
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class CaptionTest {
    @Test
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

    @Test
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
