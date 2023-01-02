package noctiluca.features.authentication.organisms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
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
import noctiluca.components.model.LoadState
import noctiluca.components.utils.openBrowser
import noctiluca.features.authentication.getDrawables
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.state.rememberAuthorizeUri
import noctiluca.features.authentication.state.rememberMastodonInstance
import noctiluca.features.authentication.state.rememberMastodonInstanceSuggests
import noctiluca.instance.model.Instance
import noctiluca.model.Hostname
import noctiluca.model.Uri

internal val InstanceSaver = listSaver(
    save = {
        val instance: Instance? = it.getValueOrNull()

        println("save: $instance")
        if (instance == null)
            listOf()
        else
            listOf(instance.name, instance.domain, instance.description, instance.thumbnail?.value, instance.languages.toTypedArray(), instance.users, instance.statuses, instance.version?.toString())
    },
    restore = {
        println("restore: $it")
        if (it.isEmpty())
            LoadState.Initial
        else
            @Suppress("UNCHECKED_CAST")
            LoadState.Loaded(Instance(it[0] as String, it[1] as String, it[2] as? String, (it[3] as? String)?.let(::Uri), (it[4] as Array<String>).toList(), it[5] as Int, it[6] as Int, (it[7] as? String)?.let { v -> Instance.Version(v) }))
    },
)

@Composable
fun SearchInstanceList(
    query: String,
    onChangeLoading: (Boolean) -> Unit,
    onSelect: (Instance.Suggest) -> Unit,
    modifier: Modifier = Modifier,
) {
    val suggests by rememberMastodonInstanceSuggests(query)
    val instance = rememberMastodonInstance(InstanceSaver)
    val authorizeUri = rememberAuthorizeUri()

    LaunchedEffect(suggests.loading, instance.loading) {
        onChangeLoading(suggests.loading || instance.loading)
    }

    val selected = instance.getValueOrNull()
    val uri = authorizeUri.getValueOrNull()

    uri?.let { openBrowser(it) }

    if (selected == null) {
        InstanceList(
            suggests.getValueOrNull() ?: listOf(),
            suggests.loaded,
            onSelect = {
                onSelect(it)
                instance.fetch(it)
            },
            modifier = modifier,
        )
    } else {
        InstanceAuthenticationCard(
            instance.getValueOrNull(),
            onStartSignIn = { authorizeUri.request(it) },
            modifier = modifier,
        )
    }
}

@Composable
private fun InstanceList(
    instances: List<Instance.Suggest>,
    loaded: Boolean,
    onSelect: (Instance.Suggest) -> Unit,
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
                modifier = Modifier.clickable { onSelect(it) },
            )
        }
    }
}

@Composable
private fun InstanceAuthenticationCard(
    instance: Instance?,
    onStartSignIn: (Instance) -> Unit,
    modifier: Modifier = Modifier,
) {
    instance ?: return

    Column(modifier) {
        FilledCard(
            headline = { CardHeader(instance.name) },
            subhead = { CardSubhead(instance.domain) },
            supporting = { CardSupporting(instance.description ?: "") },
            actions = { AuthenticationButton(onClick = { onStartSignIn(instance) }) },
            modifier = Modifier.padding(vertical = 16.dp),
        )
    }
}

@Composable
private fun AuthenticationButton(onClick: () -> Unit) = Button(onClick = onClick) {
    Text(getString().sign_in_request_authentication)
}
