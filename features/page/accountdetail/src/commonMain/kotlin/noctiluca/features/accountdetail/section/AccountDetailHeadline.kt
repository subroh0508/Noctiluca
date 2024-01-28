package noctiluca.features.accountdetail.section

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import noctiluca.features.accountdetail.component.headline.Avatar
import noctiluca.features.accountdetail.component.headline.Header
import noctiluca.model.accountdetail.AccountAttributes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AccountDetailHeadline(
    attributes: AccountAttributes,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    Header(attributes, scrollBehavior)
    Avatar(attributes.avatar, scrollBehavior)
}
