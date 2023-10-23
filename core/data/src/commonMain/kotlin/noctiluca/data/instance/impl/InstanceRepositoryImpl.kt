package noctiluca.data.instance.impl

import noctiluca.data.instance.InstanceRepository
import noctiluca.data.status.toEntity
import noctiluca.model.StatusId
import noctiluca.model.Uri
import noctiluca.model.authentication.Instance
import noctiluca.network.instancessocial.InstancesSocialApi
import noctiluca.network.instancessocial.data.NetworkInstance
import noctiluca.network.mastodon.MastodonApiV1
import noctiluca.network.mastodon.MastodonApiV2
import noctiluca.network.mastodon.data.account.NetworkAccount
import noctiluca.network.mastodon.data.instance.NetworkV1Instance
import noctiluca.network.mastodon.data.instance.NetworkV2Instance
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
            .filterNot(NetworkInstance::dead)
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

    private fun NetworkInstance.toSuggest() = Instance.Suggest(
        name,
        info?.shortDescription,
        thumbnail?.let(::Uri),
        version?.let { Instance.Version(it) },
    )

    private fun NetworkV1Instance.toValueObject() = Instance(
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

    private fun NetworkV2Instance.toValueObject(extendedDescription: String) = Instance(
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

    private fun NetworkAccount.toAdministrator() = Instance.Administrator(
        "@$acct",
        displayName,
        Uri(url),
        Uri(avatar),
    )
}
