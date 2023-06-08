package noctiluca.features.authentication.templates.scaffold.instancedetail

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import kotlinx.coroutines.Job
import noctiluca.features.authentication.LocalNavigation
import noctiluca.features.authentication.organisms.tab.InstanceDetailScrollState
import noctiluca.features.components.molecules.scaffold.HeadlineText
import noctiluca.features.components.molecules.scaffold.HeadlineTopAppBar
import noctiluca.instance.model.Instance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InstanceDetailTopAppBar(
    domain: String,
    instance: Instance?,
    job: Job?,
    tabbedScrollState: InstanceDetailScrollState,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val navigation = LocalNavigation.current

    HeadlineTopAppBar(
        title = {
            InstanceHeaderText(
                domain,
                instance,
                tabbedScrollState,
            )
        },
        onBackPressed = {
            job?.cancel()
            navigation?.backPressed()
        },
        scrollBehavior = scrollBehavior,
    )
}

@Composable
private fun InstanceHeaderText(
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
