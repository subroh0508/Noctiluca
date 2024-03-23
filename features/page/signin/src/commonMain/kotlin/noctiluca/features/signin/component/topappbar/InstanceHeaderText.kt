package noctiluca.features.signin.component.topappbar

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import noctiluca.features.shared.components.text.HeadlineText
import noctiluca.features.signin.section.scrollableframe.InstanceDetailScrollableFrameState
import noctiluca.model.authorization.Instance

@Composable
internal fun InstanceHeaderText(
    domain: String,
    instance: Instance?,
    scrollState: InstanceDetailScrollableFrameState,
) {
    if (instance != null) {
        HeadlineText(
            instance.name,
            instance.domain,
            scrollState.lazyListState.firstVisibleItemIndex > 1,
        )
        return
    }

    Text(
        domain,
        style = MaterialTheme.typography.titleLarge,
    )
}
