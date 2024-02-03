package noctiluca.data.accountdetail.impl

import kotlinx.coroutines.flow.*
import noctiluca.data.accountdetail.AccountDetailRepository
import noctiluca.data.accountdetail.toAttributeEntity
import noctiluca.data.accountdetail.toValueObject
import noctiluca.datastore.AuthenticationTokenDataStore
import noctiluca.model.AccountId
import noctiluca.model.accountdetail.AccountAttributes
import noctiluca.model.accountdetail.Relationships
import noctiluca.network.mastodon.MastodonApiV1

internal class AccountDetailRepositoryImpl(
    private val v1: MastodonApiV1,
) : AccountDetailRepository {
    private val accountAttributeStateFlow by lazy { MutableStateFlow<AccountAttributes?>(null) }

    override fun attributes(
        id: AccountId,
    ): Flow<AccountAttributes> = flow {
        emitAll(accountAttributeStateFlow)
    }.onStart {
        accountAttributeStateFlow.value = v1.getAccount(id.value).toAttributeEntity()
    }.filterNotNull()

}
