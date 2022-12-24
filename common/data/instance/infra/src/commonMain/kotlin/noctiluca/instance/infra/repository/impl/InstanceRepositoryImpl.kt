package noctiluca.instance.infra.repository.impl

import noctiluca.api.instancessocial.InstancesSocialApi
import noctiluca.api.instancessocial.json.InstanceJson
import noctiluca.api.mastodon.MastodonApi
import noctiluca.api.mastodon.params.GetInstance
import noctiluca.instance.infra.repository.InstanceRepository
import noctiluca.instance.model.Instance
import noctiluca.model.Uri
import java.net.UnknownHostException

internal class InstanceRepositoryImpl(
    private val instancesSocialApi: InstancesSocialApi,
    private val mastodonApi: MastodonApi,
): InstanceRepository {
    override suspend fun search(
        query: String,
    ): List<Instance> = try {
        listOf(mastodonApi.getInstance(query).toValueObject())
    } catch (e: UnknownHostException) {
        instancesSocialApi.search(query)
            .instances
            .filterNot(InstanceJson::dead)
            .map { it.toValueObject() }
    }

    private fun InstanceJson.toValueObject() = Instance(
        name,
        info?.shortDescription,
        thumbnail?.let(::Uri),
        info?.languages ?: listOf(),
        users,
        statuses,
        version?.let { Instance.Version(it) },
    )

    private fun GetInstance.Response.toValueObject() = Instance(
        uri,
        shortDescription,
        thumbnail?.let(::Uri),
        languages,
        stats.userCount,
        stats.statusCount,
        Instance.Version(version),
    )
}
