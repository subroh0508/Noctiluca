package noctiluca.features.accountdetail.organisms.topappbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import noctiluca.features.accountdetail.LocalNavigator
import noctiluca.features.accountdetail.getString
import noctiluca.features.components.molecules.scaffold.HeadlineText
import noctiluca.features.components.molecules.scaffold.LargeHeadlineTopAppBar
import noctiluca.model.accountdetail.AccountAttributes

private const val CONTENT_SCROLL_OFFSET = -400F

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountHeaderTopAppBar(
    detail: AccountAttributes?,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val navigation = LocalNavigator.current

    LargeHeadlineTopAppBar(
        { appAppBarState ->
            HeadlineText(
                detail,
                appAppBarState,
            )
        },
        onBackPressed = { navigation?.backPressed() },
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
        getString().account_detail_statuses.format(attributes.statusesCount),
        isHiddenHeadlineText,
    )
}
