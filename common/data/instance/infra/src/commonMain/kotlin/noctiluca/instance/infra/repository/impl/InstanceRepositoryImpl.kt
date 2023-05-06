package noctiluca.instance.infra.repository.impl

import noctiluca.api.instancessocial.InstancesSocialApi
import noctiluca.api.instancessocial.json.InstanceJson
import noctiluca.api.mastodon.MastodonApiV1
import noctiluca.api.mastodon.MastodonApiV2
import noctiluca.api.mastodon.json.account.AccountJson
import noctiluca.api.mastodon.json.instance.V1InstanceJson
import noctiluca.api.mastodon.json.instance.V2InstanceJson
import noctiluca.instance.infra.repository.InstanceRepository
import noctiluca.instance.model.Instance
import noctiluca.model.StatusId
import noctiluca.model.Uri
import noctiluca.status.infra.toEntity
import java.net.UnknownHostException

internal class InstanceRepositoryImpl(
    private val instancesSocialApi: InstancesSocialApi,
    private val v1: MastodonApiV1,
    private val v2: MastodonApiV2,
) : InstanceRepository {
    override suspend fun search(
        query: String,
    ): List<Instance.Suggest> = try {
        listOf(getInstance(query).toSuggest())
    } catch (@Suppress("SwallowedException") e: UnknownHostException) {
        instancesSocialApi.search(query)
            .instances
            .filterNot(InstanceJson::dead)
            .map { it.toSuggest() }
    }

    override suspend fun show(domain: String) = getInstance(domain)

    override suspend fun fetchLocalTimeline(
        domain: String,
        maxId: StatusId?,
    ) = v1.getTimelinesPublic(domain, maxId?.value).map {
        it.toEntity(accountId = null)
    }

    private suspend fun getInstance(domain: String): Instance {
        val result = runCatching { v1.getInstance(domain) }
        val exception = result.exceptionOrNull()
        if (exception is UnknownHostException) {
            throw exception
        }

        val v1Instance = result.getOrNull()

        if (v1Instance == null || v1Instance.version.startsWith("4")) {
            val extendedDescription = v1.getInstanceExtendedDescription(domain).content

            return v2.getInstance(domain).toValueObject(extendedDescription)
        }

        return v1Instance.toValueObject()
    }

    private fun Instance.toSuggest() = Instance.Suggest(
        domain,
        description,
        thumbnail,
        version,
    )

    private fun InstanceJson.toSuggest() = Instance.Suggest(
        name,
        info?.shortDescription,
        thumbnail?.let(::Uri),
        version?.let { Instance.Version(it) },
    )

    private fun V1InstanceJson.toValueObject() = Instance(
        title,
        uri,
        shortDescription,
        thumbnail?.let(::Uri),
        languages,
        null,
        contactAccount.toAdministrator(),
        rules?.map { Instance.Rule(it.id, it.text) } ?: listOf(),
        null,
        Instance.Version(version),
    )

    private fun V2InstanceJson.toValueObject(extendedDescription: String) = Instance(
        title,
        domain,
        description,
        thumbnail.url.let(::Uri),
        languages,
        usage.users.activeMonth,
        contact.account.toAdministrator(),
        rules.map { Instance.Rule(it.id, it.text) },
        extendedDescription,
        Instance.Version(version),
    )

    private fun AccountJson.toAdministrator() = Instance.Administrator(
        "@$acct",
        displayName,
        Uri(url),
        Uri(avatar),
    )
}
