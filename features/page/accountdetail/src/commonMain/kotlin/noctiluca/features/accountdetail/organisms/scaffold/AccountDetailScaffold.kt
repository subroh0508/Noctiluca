package noctiluca.features.accountdetail.organisms.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import noctiluca.accountdetail.model.AccountAttributes
import noctiluca.features.accountdetail.organisms.topappbar.AccountHeaderTopAppBar
import noctiluca.features.accountdetail.state.rememberAccountDetail
import noctiluca.features.components.atoms.image.AsyncImage
import noctiluca.features.components.atoms.text.HtmlText
import noctiluca.features.shared.account.AccountName
import noctiluca.model.AccountId
import noctiluca.model.Uri

private val ExpandedTopAppBarHeight = 152.dp
private val CollapsedTopAppBarHeight = 64.dp

private val HeaderHeightOffset = -(ExpandedTopAppBarHeight - CollapsedTopAppBarHeight)

private val AvatarFrameSize = 104.dp
private val AvatarIconSize = 96.dp

private val AccountDetailScaffoldPadding = 16.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailScaffold(
    id: AccountId,
) {
    val detail by rememberAccountDetail(id)
    val (attributes, _) = detail

    ScaffoldWithHeaderAndAvatar(
        attributes?.header,
        attributes?.avatar,
        topAppBar = { scrollBehavior ->
            AccountHeaderTopAppBar(
                attributes,
                scrollBehavior,
            )
        },
    ) {
        AccountDetailCaption(attributes)

        repeat(50) {
            Text(
                "item $it",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
            )
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
    content: @Composable ColumnScope.() -> Unit,
) = Scaffold(
    topBar = { topAppBar(scrollBehavior) },
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
) { paddingValues ->
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                start = AccountDetailScaffoldPadding,
                end = AccountDetailScaffoldPadding,
            )
            .verticalScroll(rememberScrollState()),
    ) { content() }

    AsyncImage(
        header,
        ContentScale.FillHeight,
        modifier = Modifier.height(ExpandedTopAppBarHeight)
            .graphicsLayer {
                translationY = calculateTranslationY(scrollBehavior)
            },
    )

    Box(
        modifier = Modifier.size(AvatarFrameSize)
            .offset(
                x = AccountDetailScaffoldPadding,
                y = ExpandedTopAppBarHeight - AvatarFrameSize / 2,
            )
            .graphicsLayer {
                translationY = calculateTranslationY(scrollBehavior)
                alpha = 1.0F - scrollBehavior.state.collapsedFraction
            }
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp),
            ),
        contentAlignment = Alignment.Center,
    ) {
        AsyncImage(
            avatar,
            modifier = Modifier.size(AvatarIconSize)
                .clip(RoundedCornerShape(8.dp)),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private fun GraphicsLayerScope.calculateTranslationY(
    scrollBehavior: TopAppBarScrollBehavior,
): Float {
    val offset = HeaderHeightOffset.toPx()
    return if (offset < scrollBehavior.state.heightOffset) scrollBehavior.state.heightOffset else offset
}

@Composable
private fun AccountDetailCaption(
    attributes: AccountAttributes?,
) = Column(
    modifier = Modifier.fillMaxWidth(),
) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        OutlinedButton(
            onClick = { /*TODO*/ },
        ) {
            Text("プロフィールを編集")
        }
    }

    if (attributes != null) {
        AccountName(
            attributes.displayName,
            attributes.screen,
        )
        Spacer(modifier = Modifier.height(8.dp))
        HtmlText(
            attributes.note,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth(),
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                attributes.followingCount.toString(),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )
            Text(
                "follows",
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                attributes.followersCount.toString(),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )
            Text(
                "followers",
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}
