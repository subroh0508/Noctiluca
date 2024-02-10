package noctiluca.features.authentication.model

import noctiluca.features.shared.model.LoadState
import noctiluca.model.authentication.Instance
import noctiluca.model.status.Status

data class InstanceDetailModel(
    val instance: Instance? = null,
    val statuses: List<Status> = listOf(),
    val instanceLoadState: LoadState = LoadState.Initial,
    val statusesLoadState: LoadState = LoadState.Initial,
)
