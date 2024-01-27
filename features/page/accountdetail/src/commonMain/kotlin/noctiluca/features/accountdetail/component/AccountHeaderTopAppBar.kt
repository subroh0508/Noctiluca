package noctiluca.features.accountdetail.component

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.navigator.LocalNavigator
import noctiluca.features.accountdetail.getString
import noctiluca.features.shared.molecules.scaffold.HeadlineText
import noctiluca.model.accountdetail.AccountAttributes

private const val CONTAINER_COLOR_ALPHA = 0.75F
private const val CONTENT_SCROLL_OFFSET = -300F

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountHeaderTopAppBar(
    detail: AccountAttributes?,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val navigator = LocalNavigator.current
    val alpha by rememberScrolledContainerColorAlpha(scrollBehavior)

    LargeTopAppBar(
        { HeadlineText(detail, scrollBehavior.state) },
        modifier = Modifier.background(
            Brush.verticalGradient(
                colors = listOf(
                    Color.Black.copy(alpha = CONTAINER_COLOR_ALPHA),
                    Color.Black.copy(alpha = alpha),
                ),
            ),
        ),
        navigationIcon = {
            IconButton(onClick = { navigator?.pop() }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                )
            }
        },
        actions = {},
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
        ),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun rememberScrolledContainerColorAlpha(
    scrollBehavior: TopAppBarScrollBehavior,
): State<Float> = remember {
    derivedStateOf { scrollBehavior.state.collapsedFraction * CONTAINER_COLOR_ALPHA }
}
