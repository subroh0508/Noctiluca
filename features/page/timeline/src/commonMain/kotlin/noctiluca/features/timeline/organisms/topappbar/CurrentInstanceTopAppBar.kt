package noctiluca.features.timeline.organisms.topappbar

import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
            IconButton(onClick = {}) {
                AsyncImage(
                    account.current?.avatar,
                    //fallback = imageResources(getDrawables().icon_mastodon),
                    modifier = Modifier.size(NavigateIconSize),
                )
            }
        }
    )
}
