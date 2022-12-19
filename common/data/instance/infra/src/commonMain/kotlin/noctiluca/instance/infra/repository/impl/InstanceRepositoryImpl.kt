package noctiluca.instance.infra.repository.impl

import noctiluca.api.instancessocial.InstancesSocialApi
import noctiluca.api.instancessocial.json.InstanceJson
import noctiluca.instance.infra.repository.InstanceRepository
import noctiluca.instance.model.Instance
import noctiluca.model.Uri

internal class InstanceRepositoryImpl(
    private val api: InstancesSocialApi,
): InstanceRepository {
    override suspend fun search(
        query: String,
    ) = api.search(query).instances.map { it.toValueObject() }

    private fun InstanceJson.toValueObject() = Instance(
        name,
        info?.shortDescription,
        thumbnail?.let(::Uri),
        info?.languages ?: listOf(),
        users,
        statuses,
        version?.let { Instance.Version(it) },
    )
}
