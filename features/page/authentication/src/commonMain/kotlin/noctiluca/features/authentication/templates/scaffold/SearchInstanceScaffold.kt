package noctiluca.features.authentication.templates.scaffold

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.authentication.SignInNavigation
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.organisms.list.InstanceSuggestsList
import noctiluca.features.authentication.organisms.textfield.SearchInstanceQueryTextField
import noctiluca.features.components.atoms.appbar.TopAppBar
import noctiluca.features.components.molecules.HeadlineWithProgress

private val HorizontalPadding = 16.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchInstanceScaffold(
    navigation: SignInNavigation,
) = Scaffold(
    topBar = { TopAppBar(getString().sign_in_page_title) },
) { paddingValues ->
    SearchInstanceQueryTextField(
        paddingValues = paddingValues,
        modifier = Modifier.padding(horizontal = HorizontalPadding),
        headline = { loading -> Headline(loading) },
        listContent = { suggests ->
            InstanceSuggestsList(
                suggests,
                onSelect = { navigation.navigateToInstanceDetail(it.domain) },
            )
        },
    )
}

@Composable
private fun Headline(loading: Boolean) = HeadlineWithProgress(
    getString().sign_in_search_instance,
    loading,
    Modifier.padding(
        top = 12.dp,
        start = HorizontalPadding,
        end = HorizontalPadding,
    ),
)
