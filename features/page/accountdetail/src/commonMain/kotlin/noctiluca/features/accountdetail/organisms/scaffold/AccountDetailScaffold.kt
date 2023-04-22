package noctiluca.features.accountdetail.organisms.scaffold

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
import noctiluca.features.components.molecules.list.items
import noctiluca.features.components.molecules.scaffold.HeadlineHeader
import noctiluca.features.components.molecules.scaffold.HeadlineAvatar
import noctiluca.features.components.molecules.scaffold.HeadlinedScaffold
import noctiluca.features.components.utils.format
import noctiluca.features.shared.account.AccountName
import noctiluca.features.shared.status.Status
import noctiluca.model.AccountId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailScaffold(
    id: AccountId,
) {
    val detail by rememberAccountDetail(id)
    val statuses = rememberAccountStatuses(id)
    val statusesScrollState = rememberTabbedAccountStatusesState(statuses.value.tab)

    val (attributes) = detail

    HeadlinedScaffold(
        statusesScrollState.lazyListState,
        topAppBar = { scrollBehavior ->
            AccountHeaderTopAppBar(
                attributes,
                scrollBehavior,
            )
        },
        header = { scrollBehavior ->
            HeadlineHeader(
                attributes?.header,
                scrollBehavior,
            )
        },
        avatar = { scrollBehavior ->
            HeadlineAvatar(
                attributes?.avatar,
                scrollBehavior,
            )
        },
        tabs = {
            AccountStatusesTabs(
                statuses,
                statusesScrollState,
            )
        },
    ) { tabs, horizontalPadding ->
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
