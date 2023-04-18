package noctiluca.features.accountdetail.organisms.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import noctiluca.features.accountdetail.getString
import noctiluca.features.accountdetail.organisms.tab.AccountStatusesTabs
import noctiluca.features.accountdetail.organisms.topappbar.AccountHeaderTopAppBar
import noctiluca.features.accountdetail.state.rememberAccountDetail
import noctiluca.features.accountdetail.state.rememberAccountStatuses
import noctiluca.features.components.atoms.card.FilledCard
import noctiluca.features.components.atoms.divider.Divider
import noctiluca.features.components.atoms.image.AsyncImage
import noctiluca.features.components.atoms.text.HtmlText
import noctiluca.features.components.molecules.list.LazyColumn
import noctiluca.features.components.molecules.list.items
import noctiluca.features.components.utils.format
import noctiluca.features.shared.account.AccountName
import noctiluca.features.shared.status.Status
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
    val statuses = rememberAccountStatuses(id)
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
        stickyHeader = { AccountStatusesTabs(statuses) },
    ) { stickyHeaderLayout ->
        item { AccountDetailCaption(attributes) }
        item { stickyHeaderLayout() }
        items(
            statuses.value.foreground,
            key = { _, status -> status.id.value },
            showDivider = true,
        ) { _, status ->
            Status(
                status,
                onClickAction = { },
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
    stickyHeader: @Composable () -> Unit = {},
    content: LazyListScope.(@Composable () -> Unit) -> Unit,
) = Scaffold(
    topBar = { topAppBar(scrollBehavior) },
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
) { paddingValues ->
    val lazyListState = rememberLazyListState()

    LazyColumn(
        state = lazyListState,
        contentPadding = paddingValues,
        modifier = Modifier.fillMaxSize(),
    ) { content(stickyHeader) }

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

    if (lazyListState.firstVisibleItemIndex > 0) {
        Box(
            modifier = Modifier.offset(y = CollapsedTopAppBarHeight),
        ) {
            stickyHeader()
        }
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
    modifier = Modifier.fillMaxWidth()
        .padding(horizontal = AccountDetailScaffoldPadding),
) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        OutlinedButton(
            onClick = { /*TODO*/ },
        ) {
            Text(getString().account_detail_profile_edit)
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

        CustomFields(attributes.fields)

        Spacer(modifier = Modifier.height(16.dp))

        RelationshipCount(
            attributes.followingCount,
            attributes.followersCount,
        )
    }

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun RelationshipCount(
    followingCount: Int,
    followersCount: Int,
) = Row(
    horizontalArrangement = Arrangement.spacedBy(4.dp),
) {
    Text(
        format(followingCount),
        style = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.Bold,
        ),
    )
    Text(
        getString().account_detail_follows,
        color = MaterialTheme.colorScheme.outline,
        style = MaterialTheme.typography.bodyLarge,
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text(
        format(followersCount),
        style = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.Bold,
        ),
    )
    Text(
        getString().account_detail_followers,
        color = MaterialTheme.colorScheme.outline,
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Composable
private fun CustomFields(
    fields: List<AccountAttributes.Field>,
) = FilledCard {
    fields.forEachIndexed { i, field ->
        if (i != 0) {
            Divider(modifier = Modifier.fillMaxWidth())
        }

        CustomField(field)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomField(
    field: AccountAttributes.Field,
) = ListItem(
    overlineText = { Text(field.name) },
    headlineText = { HtmlText(field.value) },
    colors = ListItemDefaults.colors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    ),
)
