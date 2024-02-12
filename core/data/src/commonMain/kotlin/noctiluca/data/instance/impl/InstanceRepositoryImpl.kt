package noctiluca.data.instance.impl

import kotlinx.coroutines.flow.*
import noctiluca.data.instance.InstanceRepository
import noctiluca.data.status.toEntity
import noctiluca.model.Uri
import noctiluca.model.signin.Instance
import noctiluca.model.status.Status
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
    private val suggests by lazy { MutableStateFlow<List<Instance.Suggest>>(listOf()) }
    private val instance by lazy { MutableStateFlow<Instance?>(null) }
    private val statuses by lazy { MutableStateFlow<List<Status>>(listOf()) }

    override fun suggests() = suggests
    override fun instance() = instance
    override fun statuses() = statuses

    override suspend fun search(query: String) {
        if (query.isBlank()) {
            suggests.value = listOf()
            return
        }

        try {
            suggests.value = listOf(getInstance(query).toSuggest())
        } catch (@Suppress("SwallowedException") e: UnknownHostException) {
            suggests.value = instancesSocialApi.search(query)
                .instances
                .filterNot(NetworkInstance::dead)
                .map { it.toSuggest() }
        }
    }

    override suspend fun fetchInstance(domain: String) {
        instance.value = null
        instance.value = getInstance(domain)
    }

    override suspend fun fetchStatuses(domain: String) {
        statuses.value = listOf()
        statuses.value = v1.getTimelinesPublic(domain)
            .map { it.toEntity(accountId = null) }
    }

    override suspend fun fetchMoreStatuses(domain: String) {
        statuses.value += v1.getTimelinesPublic(
            domain,
            statuses.value.lastOrNull()?.id?.value,
        ).map { it.toEntity(accountId = null) }
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
