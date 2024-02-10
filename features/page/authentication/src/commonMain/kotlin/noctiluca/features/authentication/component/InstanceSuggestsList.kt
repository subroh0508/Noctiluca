package noctiluca.features.authentication.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.organisms.card.SuggestCard
import noctiluca.model.authentication.Instance

@Composable
internal fun InstanceSuggestsList(
    suggests: List<Instance.Suggest>,
    isLoaded: Boolean,
    onSelect: (Instance.Suggest) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isLoaded && suggests.isEmpty()) {
        ListItem(
            { Text(getString().sign_in_search_instances_empty) },
            modifier = modifier,
        )
        return
    }

    LazyColumn(
        modifier,
        rememberLazyListState(),
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 8.dp,
        ),
    ) {
        items(suggests, key = { it.domain }) { suggest ->
            SuggestCard(
                suggest,
                onSelect,
                modifier = Modifier.padding(bottom = 16.dp),
            )
        }
    }
}
