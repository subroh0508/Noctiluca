package noctiluca.data.accountdetail

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import noctiluca.model.AccountId
import noctiluca.model.AuthorizedUser
import noctiluca.model.Uri
import noctiluca.model.account.Account
import noctiluca.model.accountdetail.AccountAttributes
import noctiluca.model.accountdetail.Relationship
import noctiluca.model.accountdetail.Relationships
import noctiluca.network.mastodon.data.account.NetworkAccount
import noctiluca.network.mastodon.data.account.NetworkField
import noctiluca.network.mastodon.data.account.NetworkRelationship

internal fun NetworkAccount.toAttributeEntity() = AccountAttributes(
    AccountId(id),
    username,
    displayName,
    Uri(url),
    Uri(avatar),
    if (!header.contains(NetworkAccount.MISSING_IMAGE_NAME)) Uri(header) else null,
    "@$acct",
    note,
    followersCount,
    followingCount,
    statusesCount,
    locked,
    bot,
    Relationships.NONE,
    condition,
    fields.map { it.toValueObject() },
    createdAt.toInstant().toLocalDateTime(TimeZone.of("Asia/Tokyo")),
    moved?.toAccount(),
)

internal fun NetworkRelationship.toValueObject(current: AuthorizedUser?): Relationships {
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

private fun NetworkField.toValueObject() = AccountAttributes.Field(name, value)

private val NetworkAccount.condition
    get() = when {
        limited == true -> AccountAttributes.Condition.LIMITED
        suspended == true -> AccountAttributes.Condition.SUSPENDED
        else -> null
    }

private fun NetworkAccount.toAccount() = Account(
    AccountId(id),
    username,
    displayName,
    Uri(url),
    Uri(avatar),
    "@$acct",
)
