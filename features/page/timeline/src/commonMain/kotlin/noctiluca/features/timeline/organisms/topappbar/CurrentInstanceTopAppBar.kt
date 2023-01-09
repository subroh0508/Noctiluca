package noctiluca.features.timeline.organisms.topappbar

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import noctiluca.features.components.atoms.appbar.NavigateIconSize
import noctiluca.features.components.atoms.appbar.TopAppBar
import noctiluca.features.components.atoms.image.AsyncImage
import noctiluca.features.timeline.getString
import noctiluca.features.timeline.state.rememberCurrentAuthorizedAccountStatus

@Composable
fun CurrentInstanceTopAppBar(
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
) {
    val account by rememberCurrentAuthorizedAccountStatus()

    TopAppBar(
        account.domain?.value ?: getString().timeline_page_title,
        navigationIcon = {
            IconButton(
                onClick = {},
                modifier = Modifier.padding(start = 8.dp),
            ) {
                AsyncImage(
                    account.current?.avatar,
                    //fallback = imageResources(getDrawables().icon_mastodon),
                    modifier = Modifier.size(NavigateIconSize)
                        .clip(RoundedCornerShape(8.dp)),
                )
            }
        },
        scrollBehavior = topAppBarScrollBehavior,
    )
}
