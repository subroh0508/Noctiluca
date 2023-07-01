package noctiluca.features.authentication.templates.scaffold

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import noctiluca.features.authentication.LocalAuthorizeResult
import noctiluca.features.authentication.LocalNavigation
import noctiluca.features.authentication.organisms.tab.InstanceDetailTabs
import noctiluca.features.authentication.organisms.tab.extendeddescription.InstanceExtendedDescriptionTab
import noctiluca.features.authentication.organisms.tab.info.InstanceInformationTab
import noctiluca.features.authentication.organisms.tab.localtimeline.InstanceLocalTimelineTab
import noctiluca.features.authentication.organisms.tab.rememberTabbedInstanceDetailState
import noctiluca.features.authentication.state.*
import noctiluca.features.authentication.state.rememberLocalTimelineState
import noctiluca.features.authentication.state.rememberMastodonInstanceDetail
import noctiluca.features.authentication.templates.scaffold.instancedetail.InstanceDetailActionButtons
import noctiluca.features.authentication.templates.scaffold.instancedetail.InstanceDetailHeader
import noctiluca.features.authentication.templates.scaffold.instancedetail.InstanceDetailTopAppBar
import noctiluca.features.authentication.viewmodel.MastodonInstanceDetailViewModel
import noctiluca.features.components.atoms.card.CardHeader
import noctiluca.features.components.atoms.card.CardSupporting
import noctiluca.features.components.atoms.card.FilledCard
import noctiluca.features.components.atoms.snackbar.LocalSnackbarHostState
import noctiluca.features.components.atoms.snackbar.showSnackbar
import noctiluca.features.components.getCommonString
import noctiluca.features.components.molecules.scaffold.*
import noctiluca.features.components.utils.description
import noctiluca.features.components.utils.label
import noctiluca.instance.model.Instance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InstanceDetailScaffold(
    domain: String,
    context: ComponentContext,
) {
    val viewModel = MastodonInstanceDetailViewModel.Factory(
        domain,
        LifecycleRegistry(),
        context,
    )

    LaunchedEffect(domain) { viewModel.load() }

    val uiModel by viewModel.uiModel.subscribeAsState()
    val tabbedScrollState = rememberTabbedInstanceDetailState(uiModel.instance.getValueOrNull())

    SnackbarForAuthorizationError()

    LoadStateSmallHeadlinedScaffold<Instance>(
        uiModel.instance,
        tabbedScrollState.lazyListState,
        tabComposeIndex = 3,
        snackbarHostState = LocalSnackbarHostState.current,
        topAppBar = { scrollBehavior, job, instance ->
            InstanceDetailTopAppBar(
                domain,
                instance,
                job,
                tabbedScrollState,
                scrollBehavior,
            )
        },
        bottomBar = { instance, horizontalPadding ->
            InstanceDetailActionButtons(
                instance,
                horizontalPadding,
            )
        },
        tabs = { InstanceDetailTabs(tabbedScrollState) },
        fallback = { error, paddingValues ->
            Fallback(
                error,
                paddingValues,
            )
        },
    ) { instance, tabs, horizontalPadding ->
        InstanceDetailHeader(instance, tabs, horizontalPadding)
        when (tabbedScrollState.tab) {
            InstancesTab.INFO -> item { InstanceInformationTab(instance) }
            InstancesTab.EXTENDED_DESCRIPTION -> item { InstanceExtendedDescriptionTab(instance) }
            InstancesTab.LOCAL_TIMELINE -> InstanceLocalTimelineTab(
                uiModel.statuses,
                loadMore = { viewModel.loadMore() }
            )
        }
    }
}

@Composable
private fun Fallback(
    error: Throwable?,
    paddingValues: PaddingValues,
) {
    error ?: return

    val navigation = LocalNavigation.current

    FilledCard(
        headline = { CardHeader(error.label()) },
        supporting = { CardSupporting(error.description()) },
        actions = {
            Button(
                onClick = { navigation?.backPressed() },
            ) {
                Text(getCommonString().back)
            }
        },
        modifier = Modifier.padding(
            top = paddingValues.calculateTopPadding(),
            start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
            end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
        ),
    )
}

@Composable
private fun SnackbarForAuthorizationError() {
    val error = LocalAuthorizeResult.current?.getErrorOrNull() ?: return

    showSnackbar(error.message ?: getCommonString().error_unknown)
}
