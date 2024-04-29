package noctiluca.features.signin.screen

import androidx.compose.material3.*
import androidx.compose.runtime.*
import noctiluca.features.signin.component.ActionButtons
import noctiluca.features.signin.component.InstanceDetailTabs
import noctiluca.features.signin.component.InstancesTab
import noctiluca.features.signin.component.tab.extendeddescription.InstanceExtendedDescriptionTab
import noctiluca.features.signin.component.tab.info.InstanceInformationTab
import noctiluca.features.signin.component.tab.localtimeline.InstanceLocalTimelineTab
import noctiluca.features.signin.section.InstanceDetailCaption
import noctiluca.features.signin.section.InstanceDetailScrollableFrame
import noctiluca.features.signin.section.InstanceDetailTopAppBar
import noctiluca.features.signin.viewmodel.MastodonInstanceDetailViewModel
import noctiluca.model.authorization.Instance

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
        uiModel.tab,
        uiModel.tabList,
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
            InstanceDetailTabs(
                uiModel.tab,
                uiModel.tabList,
                scrollableFrameState,
                onSwitch = { tab -> viewModel.switch(tab) },
            )
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

        when (uiModel.tab) {
            InstancesTab.Info -> InstanceInformationTab(uiModel.instance)
            InstancesTab.ExtendedDescription -> InstanceExtendedDescriptionTab(uiModel.instance)
            InstancesTab.LocalTimeline -> InstanceLocalTimelineTab(
                uiModel.statuses,
                uiModel.statusesLoadState.loading,
                loadMore = { viewModel.loadMoreStatuses(domain) },
            )
        }
    }
}
