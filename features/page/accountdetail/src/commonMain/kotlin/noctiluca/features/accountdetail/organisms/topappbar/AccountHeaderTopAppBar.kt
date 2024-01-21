package noctiluca.features.accountdetail.organisms.topappbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.LocalNavigator
import noctiluca.features.page.accountdetail.generated.resources.Res
import noctiluca.features.shared.molecules.scaffold.HeadlineText
import noctiluca.features.shared.molecules.scaffold.LargeHeadlineTopAppBar
import noctiluca.model.accountdetail.AccountAttributes
import org.jetbrains.compose.resources.stringResource

private const val CONTENT_SCROLL_OFFSET = -400F

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountHeaderTopAppBar(
    detail: AccountAttributes?,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val navigator = LocalNavigator.current

    LargeHeadlineTopAppBar(
        { appAppBarState ->
            HeadlineText(
                detail,
                appAppBarState,
            )
        },
        onBackPressed = { navigator?.pop() },
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Edit",
                )
            }
        },
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HeadlineText(
    attributes: AccountAttributes?,
    topAppBarState: TopAppBarState,
) {
    attributes ?: return

    val isHiddenHeadlineText by remember {
        derivedStateOf { topAppBarState.contentOffset < CONTENT_SCROLL_OFFSET }
    }

    HeadlineText(
        attributes.displayName,
        stringResource(Res.string.account_detail_statuses, attributes.statusesCount),
        isHiddenHeadlineText,
    )
}
