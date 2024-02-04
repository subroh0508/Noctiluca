package noctiluca.features.accountdetail.section

import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.navigator.LocalNavigator
import noctiluca.features.accountdetail.component.topappbar.ActionMenu
import noctiluca.features.accountdetail.component.topappbar.Title
import noctiluca.features.accountdetail.model.RelationshipsModel
import noctiluca.features.navigation.component.Back
import noctiluca.model.accountdetail.AccountAttributes

private const val CONTAINER_COLOR_ALPHA = 0.75F

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailTopAppBar(
    detail: AccountAttributes?,
    relationshipsModel: RelationshipsModel,
    scrollBehavior: TopAppBarScrollBehavior,
    mute: () -> Unit,
    block: () -> Unit,
    report: () -> Unit,
    toggleReblogs: () -> Unit,
) {
    val navigator = LocalNavigator.current
    val alpha by rememberScrolledContainerColorAlpha(scrollBehavior)

    LargeTopAppBar(
        { Title(detail, scrollBehavior.state) },
        modifier = Modifier.background(
            Brush.verticalGradient(
                colors = listOf(
                    Color.Black.copy(alpha = CONTAINER_COLOR_ALPHA),
                    Color.Black.copy(alpha = alpha),
                ),
            ),
        ),
        navigationIcon = { Back() },
        actions = {
            ActionMenu(
                detail?.username,
                detail?.condition,
                relationshipsModel,
                openAddList = {},
                openBrowser = {},
                mute = mute,
                block = block,
                report = report,
                toggleReblogs = toggleReblogs,
            )
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
        ),
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun rememberScrolledContainerColorAlpha(
    scrollBehavior: TopAppBarScrollBehavior,
): State<Float> = remember {
    derivedStateOf { scrollBehavior.state.collapsedFraction * CONTAINER_COLOR_ALPHA }
}
