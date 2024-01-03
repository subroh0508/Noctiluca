package noctiluca.features.statusdetail.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import noctiluca.features.statusdetail.LocalResources
import noctiluca.features.shared.molecules.scaffold.HeadlineTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StatusDetailTopAppBar() {
    val navigator = LocalNavigator.current
    val res = LocalResources.current

    HeadlineTopAppBar(
        title = {
            Text(
                res.getString().status_detail_title,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        onBackPressed = {
            navigator?.pop()
        }
    )
}
