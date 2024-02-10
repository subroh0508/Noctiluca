package noctiluca.features.authentication.component.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.molecules.list.LazyColumn
import noctiluca.model.authentication.Instance

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
