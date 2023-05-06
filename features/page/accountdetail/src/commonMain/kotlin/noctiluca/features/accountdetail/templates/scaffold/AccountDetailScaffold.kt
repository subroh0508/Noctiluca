package noctiluca.features.accountdetail.templates.scaffold

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDateTime
import noctiluca.accountdetail.model.AccountAttributes
import noctiluca.features.accountdetail.getString
import noctiluca.features.accountdetail.organisms.tab.AccountStatusesTabs
import noctiluca.features.accountdetail.organisms.tab.rememberTabbedAccountStatusesState
import noctiluca.features.accountdetail.organisms.topappbar.AccountHeaderTopAppBar
import noctiluca.features.accountdetail.state.rememberAccountDetail
import noctiluca.features.accountdetail.state.rememberAccountStatuses
import noctiluca.features.components.atoms.card.FilledCard
import noctiluca.features.components.atoms.divider.Divider
import noctiluca.features.components.atoms.text.HtmlText
import noctiluca.features.components.molecules.list.infiniteScrollFooter
import noctiluca.features.components.molecules.list.items
import noctiluca.features.components.molecules.scaffold.HeadlineAvatar
import noctiluca.features.components.molecules.scaffold.HeadlineHeader
import noctiluca.features.components.molecules.scaffold.LoadStateLargeHeadlinedScaffold
import noctiluca.features.components.utils.format
import noctiluca.features.components.utils.toYearMonthDay
import noctiluca.features.shared.account.AccountName
import noctiluca.features.shared.status.Status
import noctiluca.model.AccountId

@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailScaffold(
    id: AccountId,
    onBackToPreviousScreen: () -> Unit,
) {
    val accountDetailLoadState by rememberAccountDetail(id)
    val statuses = rememberAccountStatuses(id)
    val statusesScrollState = rememberTabbedAccountStatusesState(statuses.value.tab)

    LoadStateLargeHeadlinedScaffold<AccountAttributes>(
        accountDetailLoadState.loadState,
        statusesScrollState.lazyListState,
        tabComposeIndex = 1,
        topAppBar = { scrollBehavior, _, attributes ->
            AccountHeaderTopAppBar(
                attributes,
                scrollBehavior,
                onBackPressed = { onBackToPreviousScreen() },
            )
        },
        header = { scrollBehavior, attributes ->
            HeadlineHeader(
                attributes.header,
                scrollBehavior,
            )
        },
        avatar = { scrollBehavior, attributes ->
            HeadlineAvatar(
                attributes.avatar,
                scrollBehavior,
            )
        },
        tabs = {
            AccountStatusesTabs(
                statuses,
                statusesScrollState,
            )
        },
    ) { attributes, tabs, horizontalPadding ->
        item {
            AccountDetailCaption(
                attributes,
                modifier = Modifier.padding(horizontal = horizontalPadding),
            )
        }
        item { tabs() }
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

        infiniteScrollFooter(
            isLoading = false,
            onLoad = {
                if (statuses.value.foreground.isNotEmpty()) {
                    statuses.loadMore(this)
                }
            },
        )
    }
}

@Composable
private fun AccountDetailCaption(
    attributes: AccountAttributes?,
    modifier: Modifier = Modifier,
) = Column(
    modifier = Modifier.fillMaxWidth()
        .then(modifier),
) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()
            .padding(top = 24.dp, bottom = 8.dp),
    ) {
        RelationshipCount(
            attributes?.followingCount ?: 0,
            attributes?.followersCount ?: 0,
        )
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

        CustomFields(
            attributes.createdAt,
            attributes.fields,
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
    createdAt: LocalDateTime,
    fields: List<AccountAttributes.Field>,
) = FilledCard {
    val items = listOf(
        AccountAttributes.Field(
            name = getString().account_detail_account_created_at,
            value = createdAt.toYearMonthDay(),
        ),
    ) + fields

    items.forEachIndexed { i, field ->
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
