package noctiluca.features.authentication.templates

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import noctiluca.features.components.atoms.image.AsyncImage
import noctiluca.features.components.atoms.image.imageResources
import noctiluca.features.components.atoms.list.LeadingAvatarContainerSize
import noctiluca.features.components.atoms.list.ThreeLineListItem
import noctiluca.features.components.molecules.HeadlineWithProgress
import noctiluca.features.authentication.getDrawables
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.model.QueryText
import noctiluca.features.authentication.organisms.card.InstanceCard
import noctiluca.features.authentication.organisms.textfield.SearchInstanceQueryTextField
import noctiluca.instance.model.Instance

private val horizontalPadding = 16.dp

@Composable
internal fun SelectInstanceForm(
    modifier: Modifier = Modifier,
) = Column(modifier) {
    var loading by remember { mutableStateOf(false) }

    Headline(loading)

    SearchInstanceQueryTextField(
        onLoading = { loading = it },
        modifier = Modifier.padding(horizontal = horizontalPadding),
        listContent = { suggests, query ->
            when (query.value) {
                is QueryText.Static -> InstanceCard(
                    query.value,
                    onLoading = { loading = it },
                    modifier = Modifier.padding(horizontal = horizontalPadding),
                )
                else -> InstanceSuggestsList(
                    suggests,
                    onSelect = { query.value = QueryText(it) },
                )
            }
        },
    )
}

@Composable
private fun Headline(loading: Boolean) = HeadlineWithProgress(
    getString().sign_in_search_instance,
    loading,
    Modifier.padding(
        top = 12.dp,
        start = horizontalPadding,
        end = horizontalPadding,
    ),
)

@Composable
private fun InstanceSuggestsList(
    instances: List<Instance.Suggest>,
    onSelect: (Instance.Suggest) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

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
                    focusManager.clearFocus()
                    onSelect(it)
                },
            )
        }
    }
}
