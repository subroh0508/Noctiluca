package noctiluca.features.authentication.screen

import androidx.compose.material3.*
import androidx.compose.runtime.*
import noctiluca.features.authentication.component.ActionButtons
import noctiluca.features.authentication.component.tab.InstanceDetailTabs
import noctiluca.features.authentication.component.tab.InstancesTab
import noctiluca.features.authentication.component.tab.extendeddescription.InstanceExtendedDescriptionTab
import noctiluca.features.authentication.component.tab.info.InstanceInformationTab
import noctiluca.features.authentication.component.tab.localtimeline.InstanceLocalTimelineTab
import noctiluca.features.authentication.section.InstanceDetailCaption
import noctiluca.features.authentication.section.InstanceDetailScrollableFrame
import noctiluca.features.authentication.section.InstanceDetailTopAppBar
import noctiluca.features.authentication.viewmodel.MastodonInstanceDetailViewModel
import noctiluca.model.authentication.Instance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InstanceDetailScaffold(
    viewModel: MastodonInstanceDetailViewModel,
    domain: String,
    isSignInProgress: Boolean,
    onClickAuthorize: (Instance) -> Unit,
) {
    val uiModel by viewModel.uiModel.collectAsState()

    LaunchedEffect(domain) { viewModel.load(domain) }

    InstanceDetailScrollableFrame(
        uiModel.instance,
        uiModel.instanceLoadState,
        topBar = { scrollableFrameState, scrollBehavior ->
            InstanceDetailTopAppBar(
                domain,
                uiModel.instance,
                scrollableFrameState,
                scrollBehavior,
            )
        },
        tabs = { scrollableFrameState ->
            InstanceDetailTabs(scrollableFrameState)
        },
        bottomBar = { horizontalPadding ->
            ActionButtons(
                uiModel.instance,
                isSignInProgress,
                horizontalPadding,
                onClickAuthorize,
            )
        },
    ) { tabs, scrollableFrameState, horizontalPadding ->
        InstanceDetailCaption(
            uiModel.instance,
            { tabs(scrollableFrameState) },
            horizontalPadding,
        )

        when (scrollableFrameState.tab ?: return@InstanceDetailScrollableFrame) {
            InstancesTab.INFO -> InstanceInformationTab(uiModel.instance)
            InstancesTab.EXTENDED_DESCRIPTION -> InstanceExtendedDescriptionTab(uiModel.instance)
            InstancesTab.LOCAL_TIMELINE -> InstanceLocalTimelineTab(
                uiModel.statuses,
                uiModel.statusesLoadState.loading,
                loadMore = { viewModel.loadMoreStatuses(domain) },
            )
        }
    }
}
