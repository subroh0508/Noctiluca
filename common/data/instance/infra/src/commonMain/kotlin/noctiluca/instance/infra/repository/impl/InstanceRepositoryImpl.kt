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
import noctiluca.model.Uri
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

    private suspend fun getInstance(domain: String): Instance {
        val v1Instance = runCatching { v1.getInstance(domain) }.getOrNull()

        if (v1Instance == null || v1Instance.version.startsWith("4")) {
            return v2.getInstance(domain).toValueObject()
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
        Instance.Version(version),
    )

    private fun V2InstanceJson.toValueObject() = Instance(
        title,
        domain,
        description,
        thumbnail.url.let(::Uri),
        languages,
        usage.users.activeMonth,
        contact.account.toAdministrator(),
        rules.map { Instance.Rule(it.id, it.text) },
        Instance.Version(version),
    )

    private fun AccountJson.toAdministrator() = Instance.Administrator(
        "@$acct",
        displayName,
        Uri(url),
        Uri(avatar),
    )
}
