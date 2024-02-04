package noctiluca.features.accountdetail.section

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import noctiluca.features.accountdetail.component.headline.Avatar
import noctiluca.features.accountdetail.component.headline.Header
import noctiluca.model.accountdetail.AccountAttributes
import noctiluca.model.accountdetail.Relationships

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AccountDetailHeadline(
    attributes: AccountAttributes,
    relationships: Relationships,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    Header(attributes.header, relationships, scrollBehavior)
    Avatar(attributes.avatar, scrollBehavior)
}
