package noctiluca.features.timeline

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.intl.Locale
import noctiluca.features.components.AuthorizedFeatureComposable
import noctiluca.features.components.di.getKoinRootScope
import noctiluca.features.timeline.organisms.list.TimelineLane
import noctiluca.features.timeline.organisms.navigationbar.TimelineNavigationBar
import noctiluca.features.timeline.organisms.topappbar.CurrentInstanceTopAppBar
import noctiluca.features.timeline.state.TimelineListState
import noctiluca.features.timeline.state.rememberTimelineStatus
import org.koin.core.component.KoinScopeComponent

internal val LocalResources = compositionLocalOf { Resources("JA") }
internal val LocalScope = compositionLocalOf { getKoinRootScope() }
internal val LocalTimelineListState = compositionLocalOf { TimelineListState() }

@Composable
fun TimelineScreen(
    component: KoinScopeComponent,
    onReload: () -> Unit,
    onBackToSignIn: () -> Unit,
) = AuthorizedFeatureComposable(component, onReload, onBackToSignIn) { scope ->
    val timeline = rememberTimelineStatus(scope)

    CompositionLocalProvider(
        LocalResources provides Resources(Locale.current.language),
        LocalScope provides scope,
        LocalTimelineListState provides timeline,
    ) { TimelineScaffold() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimelineScaffold() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarScrollState())

    Scaffold(
        topBar = {
            CurrentInstanceTopAppBar(scrollBehavior)
        },
        bottomBar = {
            TimelineNavigationBar()
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) {
        TimelineLane(
            Modifier.padding(
                top = it.calculateTopPadding(),
                bottom = it.calculateBottomPadding(),
            ),
        )
    }
}
