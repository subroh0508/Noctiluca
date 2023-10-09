package noctiluca.instance.infra.repository

import noctiluca.instance.model.Instance
import noctiluca.model.StatusId
import noctiluca.status.model.Status

interface InstanceRepository {
    suspend fun search(query: String): List<Instance.Suggest>

    suspend fun show(domain: String): Instance

    suspend fun fetchLocalTimeline(
        domain: String,
        maxId: StatusId?,
    ): List<Status>
}
