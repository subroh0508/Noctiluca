package noctiluca.features.authentication.model

import noctiluca.features.shared.model.LoadState
import noctiluca.model.authentication.Instance

data class InstanceSuggestsModel(
    val query: String = "",
    val suggests: List<Instance.Suggest> = listOf(),
    val state: LoadState = LoadState.Initial,
)
