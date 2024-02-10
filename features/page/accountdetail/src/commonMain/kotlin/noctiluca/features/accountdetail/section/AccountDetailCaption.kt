package noctiluca.features.accountdetail.section

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.accountdetail.component.caption.AccountName
import noctiluca.features.accountdetail.component.caption.CustomFields
import noctiluca.features.accountdetail.component.caption.RelationshipButton
import noctiluca.features.accountdetail.component.caption.RelationshipCount
import noctiluca.features.accountdetail.model.RelationshipsModel
import noctiluca.features.shared.atoms.text.HtmlText
import noctiluca.model.accountdetail.AccountAttributes

@Composable
internal fun AccountDetailCaption(
    attributes: AccountAttributes,
    relationshipsModel: RelationshipsModel,
    follow: () -> Unit,
    block: () -> Unit,
    notifyNewStatus: () -> Unit,
    modifier: Modifier = Modifier,
) = Column(
    modifier = Modifier.fillMaxWidth()
        .then(modifier),
) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        RelationshipButton(
            attributes.locked,
            relationshipsModel,
            follow = follow,
            block = block,
            notifyNewStatus = notifyNewStatus,
        )
    }

    AccountName(attributes)

    Spacer(modifier = Modifier.height(8.dp))

    RelationshipCount(
        attributes.followingCount,
        attributes.followersCount,
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
