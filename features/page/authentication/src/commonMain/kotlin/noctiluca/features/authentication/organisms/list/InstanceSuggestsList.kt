package noctiluca.features.authentication.organisms.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.authentication.organisms.card.SuggestCard
import noctiluca.features.components.molecules.list.LazyColumn
import noctiluca.instance.model.Instance

@Composable
internal fun InstanceSuggestsList(
    instances: List<Instance.Suggest>,
    onSelect: (Instance.Suggest) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        instances,
        key = { it.domain },
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 8.dp,
        ),
        modifier = modifier,
    ) { _, suggest ->
        SuggestCard(
            suggest,
            onSelect,
            modifier = Modifier.padding(bottom = 16.dp),
        )
    }
}
