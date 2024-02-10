package noctiluca.features.authentication.component.topappbar

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import noctiluca.features.authentication.section.InstanceDetailScrollState
import noctiluca.features.shared.molecules.scaffold.HeadlineText
import noctiluca.model.authentication.Instance

@Composable
internal fun InstanceHeaderText(
    domain: String,
    instance: Instance?,
    tabbedScrollState: InstanceDetailScrollState,
) {
    if (instance != null) {
        HeadlineText(
            instance.name,
            instance.domain,
            tabbedScrollState.lazyListState.firstVisibleItemIndex > 1,
        )
        return
    }

    Text(
        domain,
        style = MaterialTheme.typography.titleLarge,
    )
}
