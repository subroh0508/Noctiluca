package noctiluca.features.timeline.template.scaffold

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import noctiluca.features.shared.toot.TootBox
import noctiluca.features.timeline.LocalNavigation
import noctiluca.features.timeline.state.rememberCurrentAuthorizedAccountStatus
import noctiluca.features.timeline.template.scaffold.toot.TootTopAppBar
import noctiluca.status.model.Status

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TootScaffold() {
    val navigation = LocalNavigation.current
    val authorizedAccountState by rememberCurrentAuthorizedAccountStatus(navigation)

    val content = remember { mutableStateOf<String?>(null) }
    val warning = remember { mutableStateOf<String?>(null) }
    val visibility = remember { mutableStateOf(Status.Visibility.PUBLIC) }

    Scaffold(
        topBar = {
            TootTopAppBar(
                visibility,
                onBackPressed = { navigation?.backPressed() },
            )
        },
    ) { paddingValues ->
        TootBox(
            authorizedAccountState.current,
            content,
            warning,
            modifier = Modifier.fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
        )
    }
}
