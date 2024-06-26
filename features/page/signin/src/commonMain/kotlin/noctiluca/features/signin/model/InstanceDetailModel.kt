package noctiluca.features.signin.model

import noctiluca.features.shared.model.LoadState
import noctiluca.features.signin.component.InstancesTab
import noctiluca.model.authorization.Instance
import noctiluca.model.status.Status

data class InstanceDetailModel(
    val instance: Instance? = null,
    val tab: InstancesTab = InstancesTab.Info,
    val statuses: List<Status> = listOf(),
    val instanceLoadState: LoadState = LoadState.Initial,
    val statusesLoadState: LoadState = LoadState.Initial,
) {
    companion object {
        private const val VERSION_REQUIRE_EXTENDED_DESCRIPTION = 4
    }

    val tabList = listOfNotNull(
        InstancesTab.Info,
        if ((instance?.version?.major ?: 0) >= VERSION_REQUIRE_EXTENDED_DESCRIPTION) {
            InstancesTab.ExtendedDescription
        } else {
            null
        },
        InstancesTab.LocalTimeline,
    )
}
