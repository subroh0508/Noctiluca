package noctiluca.features.accountdetail.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import cafe.adriel.voyager.koin.getScreenModel
import noctiluca.data.di.AuthorizedContext
import noctiluca.features.accountdetail.AccountDetailScreen
import noctiluca.features.accountdetail.model.AttributesModel
import noctiluca.features.accountdetail.model.RelationshipsModel
import noctiluca.features.accountdetail.viewmodel.AccountAttributesViewModel
import noctiluca.features.shared.extensions.getAuthorizedScreenModel
import noctiluca.model.AccountId
import noctiluca.model.accountdetail.AccountAttributes
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AccountDetailScreen.AccountDetailScaffold(
    context: AuthorizedContext,
    relationshipsModel: RelationshipsModel,
    topBar: @Composable (AccountAttributes?, TopAppBarScrollBehavior) -> Unit,
    content: @Composable (AttributesModel, PaddingValues, TopAppBarScrollBehavior) -> Unit,
) {
    val viewModel: AccountAttributesViewModel = getAuthorizedScreenModel(context) {
        parametersOf(AccountId(id))
    }
    val attributesModel by viewModel.uiModel.collectAsState()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = { topBar(attributesModel.attributes, scrollBehavior) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { paddingValues ->
        if (showProgress(attributesModel, relationshipsModel)) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
                    .offset(y = paddingValues.calculateTopPadding()),
            )
        }

        content(
            attributesModel,
            paddingValues,
            scrollBehavior,
        )
    }
}

private fun showProgress(
    attributesModel: AttributesModel,
    relationshipsModel: RelationshipsModel,
) = attributesModel.attributes == null ||
    attributesModel.state.loading ||
    relationshipsModel.state.loading
