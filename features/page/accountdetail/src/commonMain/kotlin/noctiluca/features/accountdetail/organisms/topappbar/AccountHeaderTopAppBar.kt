package noctiluca.features.accountdetail.organisms.topappbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import noctiluca.accountdetail.model.AccountAttributes
import noctiluca.features.components.atoms.appbar.HeadlineTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountHeaderTopAppBar(
    detail: AccountAttributes?,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val isScrolled by remember { derivedStateOf { scrollBehavior.state.contentOffset < -100F } }

    HeadlineTopAppBar(
        { HeadlineText(detail?.screen, detail?.statusesCount, isScrolled) },
        modifier = Modifier.background(Color.Transparent),
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = Color.Transparent,
            MaterialTheme.colorScheme.surfaceColorAtElevation(
                elevation = 3.0.dp,
            )
        ),
        scrollBehavior = scrollBehavior,
    )
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
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                "$statusesCount statuses",
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}
