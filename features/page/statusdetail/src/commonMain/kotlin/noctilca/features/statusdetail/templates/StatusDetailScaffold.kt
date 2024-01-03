package noctilca.features.statusdetail.templates

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.coroutines.Job
import noctilca.features.statusdetail.LocalResources
import noctilca.features.statusdetail.component.StatusDetailTopAppBar
import noctilca.features.statusdetail.viewmodel.StatusDetailViewModel
import noctiluca.features.shared.model.LoadState
import noctiluca.features.shared.molecules.scaffold.HeadlineTopAppBar
import noctiluca.features.shared.molecules.scaffold.LoadStateSmallHeadlinedScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StatusDetailScaffold(
    viewModel: StatusDetailViewModel,
) {
    val navigator = LocalNavigator.current
    val res = LocalResources.current

    val lazyListState = rememberLazyListState()

    LoadStateSmallHeadlinedScaffold<Any>(
        LoadState.Loading(Job()),
        lazyListState,
        topAppBar = { _, job, _ ->
            StatusDetailTopAppBar(job)
        }
    ) { data, horizontalPadding ->

    }
}
