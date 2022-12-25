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
    ): List<Instance.Suggest> = try {
        listOf(mastodonApi.getInstance(query).toSuggest())
    } catch (e: UnknownHostException) {
        instancesSocialApi.search(query)
            .instances
            .filterNot(InstanceJson::dead)
            .map { it.toSuggest() }
    }

    override suspend fun show(domain: String) = mastodonApi.getInstance(domain).toValueObject()

    private fun InstanceJson.toSuggest() = Instance.Suggest(
        name,
        info?.shortDescription,
        thumbnail?.let(::Uri),
        version?.let { Instance.Version(it) },
    )

    private fun GetInstance.Response.toSuggest() = Instance.Suggest(
        uri,
        shortDescription,
        thumbnail?.let(::Uri),
        Instance.Version(version),
    )

    private fun GetInstance.Response.toValueObject() = Instance(
        title,
        uri,
        shortDescription,
        thumbnail?.let(::Uri),
        languages,
        stats.userCount,
        stats.statusCount,
        Instance.Version(version),
    )
}
