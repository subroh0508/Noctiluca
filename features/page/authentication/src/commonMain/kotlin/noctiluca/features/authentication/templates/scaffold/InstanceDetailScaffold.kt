package noctiluca.features.authentication.templates.scaffold

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import cafe.adriel.voyager.navigator.LocalNavigator
import noctiluca.features.authentication.model.AuthorizeResult
import noctiluca.features.authentication.organisms.tab.InstanceDetailTabs
import noctiluca.features.authentication.organisms.tab.InstancesTab
import noctiluca.features.authentication.organisms.tab.extendeddescription.InstanceExtendedDescriptionTab
import noctiluca.features.authentication.organisms.tab.info.InstanceInformationTab
import noctiluca.features.authentication.organisms.tab.localtimeline.InstanceLocalTimelineTab
import noctiluca.features.authentication.organisms.tab.rememberTabbedInstanceDetailState
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
import noctiluca.model.authentication.Instance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InstanceDetailScaffold(
    viewModel: MastodonInstanceDetailViewModel,
    authorizeResult: AuthorizeResult?,
    isFetchingAccessToken: Boolean,
    onClickAuthorize: (Instance) -> Unit,
) {
    LaunchedEffect(viewModel.domain) { viewModel.load() }

    val uiModel by viewModel.uiModel.collectAsState()
    val tabbedScrollState = rememberTabbedInstanceDetailState(uiModel.instance.getValueOrNull())

    SnackbarForAuthorizationError(authorizeResult)

    LoadStateSmallHeadlinedScaffold<Instance>(
        uiModel.instance,
        tabbedScrollState.lazyListState,
        tabComposeIndex = 3,
        snackbarHostState = LocalSnackbarHostState.current,
        topAppBar = { scrollBehavior, job, instance ->
            InstanceDetailTopAppBar(
                viewModel.domain,
                instance,
                job,
                tabbedScrollState,
                scrollBehavior,
            )
        },
        bottomBar = { instance, horizontalPadding ->
            InstanceDetailActionButtons(
                instance,
                authorizeResult?.getCodeOrNull() != null && isFetchingAccessToken,
                horizontalPadding,
                onClickAuthorize,
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

    val navigator = LocalNavigator.current

    FilledCard(
        headline = { CardHeader(error.label()) },
        supporting = { CardSupporting(error.description()) },
        actions = {
            Button(
                onClick = { navigator?.pop() },
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
private fun SnackbarForAuthorizationError(
    authorizeResult: AuthorizeResult?,
) {
    val error = authorizeResult?.getErrorOrNull() ?: return

    showSnackbar(error.message ?: getCommonString().error_unknown)
}
