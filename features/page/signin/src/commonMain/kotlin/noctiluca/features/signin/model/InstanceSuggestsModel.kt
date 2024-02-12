package noctiluca.features.signin.model

import noctiluca.features.shared.model.LoadState
import noctiluca.model.authorization.Instance

data class InstanceSuggestsModel(
    val query: String = "",
    val suggests: List<Instance.Suggest> = listOf(),
    val state: LoadState = LoadState.Initial,
)
