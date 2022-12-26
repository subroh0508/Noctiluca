package noctiluca.features.authentication.organisms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.components.atoms.card.CardHeader
import noctiluca.components.atoms.card.CardSubhead
import noctiluca.components.atoms.card.CardSupporting
import noctiluca.components.atoms.card.FilledCard
import noctiluca.components.atoms.image.AsyncImage
import noctiluca.components.atoms.image.imageResources
import noctiluca.components.atoms.list.LeadingAvatarContainerSize
import noctiluca.components.atoms.list.OneLineListItem
import noctiluca.components.atoms.list.ThreeLineListItem
import noctiluca.features.authentication.getDrawables
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.state.rememberMastodonInstance
import noctiluca.features.authentication.state.rememberMastodonInstanceSuggests
import noctiluca.instance.model.Instance

@Composable
fun SearchInstanceList(
    query: String,
    domain: String,
    onChangeLoading: (Boolean) -> Unit,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val suggests by rememberMastodonInstanceSuggests(query)
    val instance by rememberMastodonInstance(domain)

    LaunchedEffect(suggests.loading, instance.loading) {
        onChangeLoading(suggests.loading || instance.loading)
    }

    if (domain.isBlank()) {
        InstanceList(
            suggests.getValueOrNull() ?: listOf(),
            suggests.loaded,
            onSelect = onSelect,
            modifier = modifier,
        )
    } else {
        InstanceAuthenticationCard(
            instance.getValueOrNull(),
            modifier = modifier,
        )
    }
}

@Composable
private fun InstanceList(
    instances: List<Instance.Suggest>,
    loaded: Boolean,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (loaded && instances.isEmpty()) {
        OneLineListItem(getString().sign_in_search_instances_empty)
        return
    }

    LazyColumn(
        state = rememberLazyListState(),
        modifier = modifier,
    ) {
        items(instances) {
            ThreeLineListItem(
                it.domain,
                supportingText = it.description ?: "",
                leadingContent = {
                    AsyncImage(
                        it.thumbnail,
                        fallback = imageResources(getDrawables().icon_mastodon),
                        modifier = Modifier.size(LeadingAvatarContainerSize),
                    )
                },
                modifier = Modifier.clickable {
                    onSelect(it.domain)
                },
            )
        }
    }
}

@Composable
private fun InstanceAuthenticationCard(
    instance: Instance?,
    modifier: Modifier = Modifier,
) {
    instance ?: return

    Column(modifier) {
        FilledCard(
            headline = { CardHeader(instance.name) },
            subhead = { CardSubhead(instance.domain) },
            supporting = { CardSupporting(instance.description ?: "") },
            actions = { AuthenticationButton() },
            modifier = Modifier.padding(vertical = 16.dp),
        )
    }
}

@Composable
private fun AuthenticationButton() {
    Button(onClick = {}) { Text(getString().sign_in_request_authentication) }
}
