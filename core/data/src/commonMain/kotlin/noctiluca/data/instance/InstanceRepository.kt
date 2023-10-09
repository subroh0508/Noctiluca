package noctiluca.data.instance

import noctiluca.model.StatusId
import noctiluca.model.authentication.Instance
import noctiluca.model.status.Status

interface InstanceRepository {
    suspend fun search(query: String): List<Instance.Suggest>

    suspend fun show(domain: String): Instance

    suspend fun fetchLocalTimeline(
        domain: String,
        maxId: StatusId?,
    ): List<Status>
}
