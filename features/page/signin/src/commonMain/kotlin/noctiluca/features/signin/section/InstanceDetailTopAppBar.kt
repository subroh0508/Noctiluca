package noctiluca.features.signin.section

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import noctiluca.features.navigation.component.Back
import noctiluca.features.signin.component.topappbar.InstanceHeaderText
import noctiluca.features.signin.section.scrollableframe.InstanceDetailScrollableFrameState
import noctiluca.model.authentication.Instance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InstanceDetailTopAppBar(
    domain: String,
    instance: Instance?,
    scrollState: InstanceDetailScrollableFrameState,
    scrollBehavior: TopAppBarScrollBehavior,
) = TopAppBar(
    title = {
        InstanceHeaderText(
            domain,
            instance,
            scrollState,
        )
    },
    navigationIcon = { Back() },
    colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surface,
        scrolledContainerColor = MaterialTheme.colorScheme.surface,
    ),
    scrollBehavior = scrollBehavior,
)
