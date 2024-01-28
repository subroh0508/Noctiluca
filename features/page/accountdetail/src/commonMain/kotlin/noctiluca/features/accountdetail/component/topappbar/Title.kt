package noctiluca.features.accountdetail.component.topappbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import noctiluca.features.accountdetail.getString
import noctiluca.features.shared.molecules.scaffold.HeadlineText
import noctiluca.model.accountdetail.AccountAttributes

private const val CONTENT_SCROLL_OFFSET = -300F

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Title(
    attributes: AccountAttributes?,
    topAppBarState: TopAppBarState,
) {
    attributes ?: return

    val isHiddenHeadlineText by remember {
        derivedStateOf { topAppBarState.contentOffset < CONTENT_SCROLL_OFFSET }
    }

    HeadlineText(
        attributes.displayName,
        getString().account_detail_statuses.format(attributes.statusesCount),
        isHiddenHeadlineText,
    )
}
