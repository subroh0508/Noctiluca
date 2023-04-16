package noctiluca.features.accountdetail.organisms.topappbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import noctiluca.accountdetail.model.AccountAttributes
import noctiluca.features.accountdetail.getString
import noctiluca.features.components.atoms.appbar.HeadlineTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountHeaderTopAppBar(
    detail: AccountAttributes?,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val isScrolled by remember { derivedStateOf { scrollBehavior.state.contentOffset < -400F } }
    val alpha by rememberScrolledContainerColorAlpha(scrollBehavior)

    HeadlineTopAppBar(
        { HeadlineText(detail?.displayName, detail?.statusesCount, isScrolled) },
        modifier = Modifier.background(Color.Transparent),
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = alpha),
        ),
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Suppress("MagicNumber")
private fun rememberScrolledContainerColorAlpha(
    scrollBehavior: TopAppBarScrollBehavior,
): State<Float> = remember {
    derivedStateOf { scrollBehavior.state.collapsedFraction * 0.75F }
}

@Composable
private fun HeadlineText(
    screen: String?,
    statusesCount: Int?,
    isScrolled: Boolean,
) {
    if (screen != null && statusesCount != null && isScrolled) {
        Column {
            Text(
                screen,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Medium,
                ),
            )
            Text(
                getString().account_detail_statuses.format(statusesCount),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Normal,
                ),
            )
        }
    }
}
