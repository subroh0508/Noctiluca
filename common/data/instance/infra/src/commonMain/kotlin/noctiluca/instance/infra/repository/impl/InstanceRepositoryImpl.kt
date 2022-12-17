package noctiluca.instance.infra.repository.impl

import noctiluca.api.instancessocial.InstancesSocialApi
import noctiluca.instance.infra.repository.InstanceRepository

internal class InstanceRepositoryImpl(
    private val api: InstancesSocialApi,
): InstanceRepository {
    override suspend fun search(query: String) = listOf(api.search(query))
}
