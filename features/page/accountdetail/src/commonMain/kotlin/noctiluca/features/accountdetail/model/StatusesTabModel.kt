package noctiluca.features.accountdetail.model

import noctiluca.features.shared.model.LoadState
import noctiluca.model.accountdetail.StatusesQuery
import noctiluca.model.status.Status

data class StatusesTabModel(
    val query: StatusesQuery = StatusesQuery.DEFAULT,
    val statuses: Map<StatusesQuery, List<Status>> = mapOf(),
    val state: LoadState = LoadState.Initial,
) {
    val foreground = statuses[query] ?: listOf()
}
