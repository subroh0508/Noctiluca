package noctiluca.features.authentication

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import noctiluca.components.FeatureComponent
import noctiluca.components.atoms.appbar.TopAppBar
import noctiluca.features.authentication.di.SignInModule
import noctiluca.features.authentication.organisms.InputAuthentication
import noctiluca.features.authentication.organisms.SearchInstanceList

internal val CurrentScope get() = SignInModule.scope
internal val LocalResources = compositionLocalOf { Resources("JA") }

@Composable
fun SignIn() = FeatureComponent(SignInModule) {
    CompositionLocalProvider(
        LocalResources provides Resources(Locale.current.language),
    ) {
        SignInScaffold()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignInScaffold() = Scaffold(
    topBar = {
        TopAppBar(getString().sign_in_page_title)
    }
) { paddingValues ->
    var domain: String? by remember { mutableStateOf(null) }

    if (domain == null) {
        SearchInstanceList(
            onSelect = { domain = it },
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
        )
        return@Scaffold
    }

    InputAuthentication(
        domain,
        modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
    )
}


