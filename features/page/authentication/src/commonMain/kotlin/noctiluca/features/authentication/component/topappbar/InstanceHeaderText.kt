package noctiluca.features.authentication.component.topappbar

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import noctiluca.features.authentication.section.scrollableframe.InstanceDetailScrollableFrameState
import noctiluca.features.shared.molecules.scaffold.HeadlineText
import noctiluca.model.authentication.Instance

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
