package noctiluca.features.accountdetail.organisms.scaffold

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import noctiluca.features.accountdetail.organisms.topappbar.AccountHeaderTopAppBar
import noctiluca.features.accountdetail.state.rememberAccountDetail
import noctiluca.features.components.atoms.image.AsyncImage
import noctiluca.model.AccountId
import noctiluca.model.Uri

private val ExpandedTopAppBarHeight = 152.dp
private val CollapsedTopAppBarHeight = 64.dp

private val HeaderHeightOffset = -(ExpandedTopAppBarHeight - CollapsedTopAppBarHeight)

private val AvatarIconSize = 96.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailScaffold(
    id: AccountId,
) {
    val detail = rememberAccountDetail(id)

    ScaffoldWithHeaderAndAvatar(
        detail.value.attributes?.header,
        detail.value.attributes?.avatar,
        topAppBar = { scrollBehavior ->
            AccountHeaderTopAppBar(
                detail.value.attributes,
                scrollBehavior,
            )
        },
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(50) {
                Text(
                    "item $it",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScaffoldWithHeaderAndAvatar(
    header: Uri?,
    avatar: Uri?,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
    topAppBar: @Composable (TopAppBarScrollBehavior) -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) = Scaffold(
    topBar = { topAppBar(scrollBehavior) },
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
) { paddingValues ->
    content(paddingValues)

    AsyncImage(
        header,
        ContentScale.FillHeight,
        modifier = Modifier.height(ExpandedTopAppBarHeight)
            .graphicsLayer {
                translationY = calculateTranslationY(scrollBehavior)
            },
    )

    AsyncImage(
        avatar,
        modifier = Modifier.size(AvatarIconSize)
            .offset(
                x = 16.dp,
                y = ExpandedTopAppBarHeight - AvatarIconSize / 2,
            )
            .graphicsLayer {
                translationY = calculateTranslationY(scrollBehavior)
                alpha = 1.0F - scrollBehavior.state.collapsedFraction
            },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
private fun GraphicsLayerScope.calculateTranslationY(
    scrollBehavior: TopAppBarScrollBehavior,
): Float {
    val offset = HeaderHeightOffset.toPx()
    return if (offset < scrollBehavior.state.heightOffset) scrollBehavior.state.heightOffset else offset
}
