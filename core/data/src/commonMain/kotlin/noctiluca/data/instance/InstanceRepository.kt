package noctiluca.data.instance

import kotlinx.coroutines.flow.Flow
import noctiluca.model.signin.Instance
import noctiluca.model.status.Status

interface InstanceRepository {
    fun suggests(): Flow<List<Instance.Suggest>>
    fun instance(): Flow<Instance?>
    fun statuses(): Flow<List<Status>>

    suspend fun search(query: String)
    suspend fun fetchInstance(domain: String)
    suspend fun fetchStatuses(domain: String)
    suspend fun fetchMoreStatuses(domain: String)
}
