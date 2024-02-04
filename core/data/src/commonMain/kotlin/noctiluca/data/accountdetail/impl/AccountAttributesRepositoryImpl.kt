package noctiluca.data.accountdetail.impl

import kotlinx.coroutines.flow.*
import noctiluca.data.accountdetail.AccountAttributesRepository
import noctiluca.data.accountdetail.toAttributeEntity
import noctiluca.model.AccountId
import noctiluca.model.accountdetail.AccountAttributes
import noctiluca.network.mastodon.MastodonApiV1

internal class AccountAttributesRepositoryImpl(
    private val v1: MastodonApiV1,
) : AccountAttributesRepository {
    private val accountAttributeStateFlow by lazy { MutableStateFlow<AccountAttributes?>(null) }

    override fun attributes(
        id: AccountId,
    ): Flow<AccountAttributes> = flow {
        emitAll(accountAttributeStateFlow)
    }.onStart {
        accountAttributeStateFlow.value = v1.getAccount(id.value).toAttributeEntity()
    }.filterNotNull()
}
