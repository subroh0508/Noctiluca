package noctiluca.features.accountdetail.component.caption

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import kotlinx.datetime.LocalDateTime
import noctiluca.features.accountdetail.getString
import noctiluca.features.shared.atoms.card.FilledCard
import noctiluca.features.shared.atoms.text.HtmlText
import noctiluca.features.shared.utils.toYearMonthDay
import noctiluca.model.accountdetail.AccountAttributes

@Composable
internal fun CustomFields(
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
            HorizontalDivider(
                thickness = Dp.Hairline,
                color = MaterialTheme.colorScheme.outline,
            )
        }

        CustomField(field)
    }
}

@Composable
private fun CustomField(
    field: AccountAttributes.Field,
) = ListItem(
    overlineContent = { Text(field.name) },
    headlineContent = { HtmlText(field.value) },
    colors = ListItemDefaults.colors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    ),
)
