package noctiluca.features.statusdetail.component

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import noctiluca.features.navigation.component.Back
import noctiluca.features.statusdetail.LocalResources

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StatusDetailTopAppBar() {
    val res = LocalResources.current

    TopAppBar(
        {
            Text(
                res.getString().status_detail_title,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        navigationIcon = { Back() },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surface,
        ),
        scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
    )
}
