package noctiluca.features.signin.mock

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import noctiluca.data.instance.InstanceRepository
import noctiluca.model.authorization.Instance
import noctiluca.model.status.Status

open class FakeInstanceRepository : InstanceRepository {
    protected val instances: MutableStateFlow<Instance?> by lazy { MutableStateFlow(null) }
    protected val statuses: MutableStateFlow<List<Status>> by lazy { MutableStateFlow(listOf()) }
    protected val suggests: MutableStateFlow<List<Instance.Suggest>> by lazy {
        MutableStateFlow(
            listOf()
        )
    }

    override fun instance(): Flow<Instance?> = instances
    override fun statuses(): Flow<List<Status>> = statuses
    override fun suggests(): Flow<List<Instance.Suggest>> = suggests
    override suspend fun fetchInstance(domain: String) = Unit
    override suspend fun fetchStatuses(domain: String) = Unit
    override suspend fun fetchMoreStatuses(domain: String) = Unit
    override suspend fun search(query: String) = Unit
}
