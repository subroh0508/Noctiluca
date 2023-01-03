package noctiluca.features.timeline.organisms.topappbar

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.components.atoms.appbar.NavigateIconSize
import noctiluca.components.atoms.appbar.TopAppBar
import noctiluca.components.atoms.image.AsyncImage
import noctiluca.components.atoms.image.imageResources
import noctiluca.features.timeline.getString
import noctiluca.features.timeline.state.rememberCurrentStatus

@Composable
fun CurrentInstanceTopAppBar() {
    val account by rememberCurrentStatus()

    println(account.current?.avatar)
    TopAppBar(
        account.hostname?.value ?: getString().timeline_page_title,
        navigationIcon = {
            IconButton(
                onClick = {},
                modifier = Modifier.padding(start = 8.dp),
            ) {
                AsyncImage(
                    account.current?.avatar,
                    //fallback = imageResources(getDrawables().icon_mastodon),
                    modifier = Modifier.size(NavigateIconSize),
                )
            }
        }
    )
}
