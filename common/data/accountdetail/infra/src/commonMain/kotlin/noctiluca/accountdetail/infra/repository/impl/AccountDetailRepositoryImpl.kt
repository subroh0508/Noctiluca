package noctiluca.accountdetail.infra.repository.impl

import noctiluca.account.model.Account
import noctiluca.accountdetail.infra.repository.AccountDetailRepository
import noctiluca.accountdetail.infra.toValueObject
import noctiluca.accountdetail.model.AccountDetail
import noctiluca.accountdetail.model.Relationship
import noctiluca.accountdetail.model.Relationships
import noctiluca.api.mastodon.MastodonApiV1
import noctiluca.api.mastodon.json.account.AccountJson
import noctiluca.api.mastodon.json.account.RelationshipJson
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Uri
import noctiluca.repository.TokenProvider

internal class AccountDetailRepositoryImpl(
    private val v1: MastodonApiV1,
    private val tokenProvider: TokenProvider,
) : AccountDetailRepository {
    override suspend fun fetch(
        id: AccountId,
    ): AccountDetail {
        val current = tokenProvider.getCurrent()
        val account = v1.getAccount(id.value)
        val relationship = if (id != current?.id && account.moved == null) Relationships.NONE else Relationships.ME

        return account.toEntity(relationship)
    }

    override suspend fun fetchRelationships(
        id: AccountId,
    ): Relationships {
        if (id == tokenProvider.getCurrent()?.id) {
            return Relationships.ME
        }

        val json = v1.getAccountsRelationships(listOf(id.value))
            .find { it.id == id.value }
            ?: return Relationships.NONE

        return json.toValueObject(tokenProvider.getCurrent())
    }

    private fun AccountJson.toEntity(
        relationships: Relationships,
    ) = AccountDetail(
        AccountId(id),
        username,
        displayName,
        Uri(url),
        Uri(avatar),
        Uri(header),
        "@$acct",
        note,
        followersCount,
        followingCount,
        statusesCount,
        locked,
        bot,
        relationships,
        condition,
        fields.map { it.toValueObject() },
        moved?.toAccount(),
    )

    private fun RelationshipJson.toValueObject(current: AuthorizedUser?): Relationships {
        if (id == current?.id?.value) {
            return Relationships.ME
        }

        return Relationships(
            setOfNotNull(
                if (following) Relationship.FOLLOWING else null,
                if (showingReblogs) Relationship.SHOWING_REBLOGS else null,
                if (notifying) Relationship.NOTIFYING else null,
                if (followedBy) Relationship.FOLLOWED_BY else null,
                if (blocking) Relationship.BLOCKING else null,
                if (blockedBy) Relationship.BLOCKED_BY else null,
                if (muting) Relationship.MUTING else null,
                if (mutingNotifications) Relationship.MUTING_NOTIFICATIONS else null,
                if (requested) Relationship.REQUESTED else null,
                if (domainBlocking) Relationship.DOMAIN_BLOCKING else null,
                if (endorsed) Relationship.ENDORSED else null,
            ),
        )
    }

    private val AccountJson.condition
        get() = when {
            limited == true -> AccountDetail.Condition.LIMITED
            suspended == true -> AccountDetail.Condition.SUSPENDED
            else -> null
        }

    private fun AccountJson.toAccount() = Account(
        AccountId(id),
        username,
        displayName,
        Uri(url),
        Uri(avatar),
        "@$acct",
    )
}
