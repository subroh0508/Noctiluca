package noctiluca.features.accountdetail.organisms.topappbar

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import noctiluca.accountdetail.model.AccountAttributes
import noctiluca.features.accountdetail.getString
import noctiluca.features.components.molecules.scaffold.LargeHeadlineTopAppBar

private const val CONTENT_SCROLL_OFFSET = -400F

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountHeaderTopAppBar(
    detail: AccountAttributes?,
    scrollBehavior: TopAppBarScrollBehavior,
    onBackPressed: () -> Unit,
) = LargeHeadlineTopAppBar(
    { appAppBarState ->
        HeadlineText(
            detail?.displayName,
            detail?.statusesCount,
            appAppBarState,
        )
    },
    onBackPressed = onBackPressed,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HeadlineText(
    screen: String?,
    statusesCount: Int?,
    topAppBarState: TopAppBarState,
) {
    val isHiddenHeadlineText by remember {
        derivedStateOf { topAppBarState.contentOffset < CONTENT_SCROLL_OFFSET }
    }

    if (screen != null && statusesCount != null && isHiddenHeadlineText) {
        Column {
            Text(
                screen,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Medium,
                ),
            )
            Text(
                getString().account_detail_statuses.format(statusesCount),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Normal,
                ),
            )
        }
    }
}
