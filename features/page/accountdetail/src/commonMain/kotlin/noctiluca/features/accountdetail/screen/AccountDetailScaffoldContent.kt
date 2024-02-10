package noctiluca.features.accountdetail.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import noctiluca.features.accountdetail.AccountDetailScreen
import noctiluca.features.accountdetail.component.AccountDetailTabs
import noctiluca.features.accountdetail.model.AttributesModel
import noctiluca.features.accountdetail.model.RelationshipsModel
import noctiluca.features.accountdetail.section.AccountDetailCaption
import noctiluca.features.accountdetail.section.AccountDetailScrollableFrame
import noctiluca.features.accountdetail.section.scrollableframe.rememberAccountDetailScrollableFrameState
import noctiluca.features.accountdetail.viewmodel.AccountStatusesViewModel
import noctiluca.features.navigation.StatusDetail
import noctiluca.features.shared.molecules.list.infiniteScrollFooter
import noctiluca.features.shared.molecules.list.items
import noctiluca.features.shared.status.Status
import noctiluca.model.AccountId
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AccountDetailScreen.AccountDetailContent(
    paddingValues: PaddingValues,
    attributesModel: AttributesModel,
    relationshipsModel: RelationshipsModel,
    scrollBehavior: TopAppBarScrollBehavior,
    follow: () -> Unit,
    block: () -> Unit,
    notifyNewStatus: () -> Unit,
) {
    val viewModel: AccountStatusesViewModel = getScreenModel {
        parametersOf(AccountId(id))
    }
    val statusesModel by viewModel.uiModel.collectAsState()

    val navigator = LocalNavigator.current

    AccountDetailScrollableFrame(
        paddingValues,
        attributesModel.attributes,
        relationshipsModel.relationships,
        rememberAccountDetailScrollableFrameState(statusesModel.query),
        scrollBehavior,
        caption = { attributes ->
            AccountDetailCaption(
                attributes,
                relationshipsModel,
                follow = follow,
                block = block,
                notifyNewStatus = notifyNewStatus,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        },
        tabs = { scrollState ->
            AccountDetailTabs(
                statusesModel.query,
                scrollState,
                onSwitch = { tab -> viewModel.switch(tab) },
            )
        },
    ) {
        items(
            statusesModel.foreground,
            key = { _, status -> status.id.value },
            showDivider = true,
        ) { _, status ->
            val statusDetail = rememberScreen(StatusDetail(status.id.value))

            Status(
                status,
                onClick = { navigator?.push(statusDetail) },
                onClickAvatar = {},
                onClickAction = {},
            )
        }

        infiniteScrollFooter(
            isLoading = statusesModel.state.loading,
            onLoad = { viewModel.loadStatusesMore() },
        )
    }
}
