package noctiluca.features.authentication.model

import noctiluca.features.authentication.component.InstancesTab
import noctiluca.features.shared.model.LoadState
import noctiluca.model.authentication.Instance
import noctiluca.model.status.Status

data class InstanceDetailModel(
    val instance: Instance? = null,
    val tab: InstancesTab = InstancesTab.INFO,
    val statuses: List<Status> = listOf(),
    val instanceLoadState: LoadState = LoadState.Initial,
    val statusesLoadState: LoadState = LoadState.Initial,
) {
    companion object {
        private const val VERSION_REQUIRE_EXTENDED_DESCRIPTION = 4
    }

    val tabList = listOfNotNull(
        InstancesTab.INFO,
        if ((instance?.version?.major ?: 0) >= VERSION_REQUIRE_EXTENDED_DESCRIPTION) {
            InstancesTab.EXTENDED_DESCRIPTION
        } else {
            null
        },
        InstancesTab.LOCAL_TIMELINE,
    )
}