package noctiluca.features.accountdetail.templates.scaffold.accountdetail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDateTime
import noctiluca.accountdetail.model.AccountAttributes
import noctiluca.features.accountdetail.getString
import noctiluca.features.components.atoms.card.FilledCard
import noctiluca.features.components.atoms.divider.Divider
import noctiluca.features.components.atoms.text.HtmlText
import noctiluca.features.components.utils.format
import noctiluca.features.components.utils.toYearMonthDay
import noctiluca.features.shared.account.AccountName

@Composable
internal fun AccountDetailCaption(
    attributes: AccountAttributes,
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
            attributes.followingCount,
            attributes.followersCount,
        )
    }

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