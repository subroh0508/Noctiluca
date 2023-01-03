package noctiluca.instance.infra.repository.impl

import noctiluca.api.instancessocial.InstancesSocialApi
import noctiluca.api.instancessocial.json.InstanceJson
import noctiluca.api.mastodon.MastodonV1Api
import noctiluca.api.mastodon.json.instance.V1InstanceJson
import noctiluca.instance.infra.repository.InstanceRepository
import noctiluca.instance.model.Instance
import noctiluca.model.Uri
import java.net.UnknownHostException

internal class InstanceRepositoryImpl(
    private val instancesSocialApi: InstancesSocialApi,
    private val v1: MastodonV1Api,
): InstanceRepository {
    override suspend fun search(
        query: String,
    ): List<Instance.Suggest> = try {
        listOf(v1.getInstance(query).toSuggest())
    } catch (e: UnknownHostException) {
        instancesSocialApi.search(query)
            .instances
            .filterNot(InstanceJson::dead)
            .map { it.toSuggest() }
    }

    override suspend fun show(domain: String) = v1.getInstance(domain).toValueObject()

    private fun InstanceJson.toSuggest() = Instance.Suggest(
        name,
        info?.shortDescription,
        thumbnail?.let(::Uri),
        version?.let { Instance.Version(it) },
    )

    private fun V1InstanceJson.toSuggest() = Instance.Suggest(
        uri,
        shortDescription,
        thumbnail?.let(::Uri),
        Instance.Version(version),
    )

    private fun V1InstanceJson.toValueObject() = Instance(
        title,
        uri,
        shortDescription,
        thumbnail?.let(::Uri),
        languages,
        stats.userCount ?: 0,
        stats.statusCount ?: 0,
        Instance.Version(version),
    )
}
