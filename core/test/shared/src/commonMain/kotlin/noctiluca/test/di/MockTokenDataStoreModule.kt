package noctiluca.test.di

import noctiluca.datastore.TokenDataStore
import noctiluca.model.AuthorizedUser
import noctiluca.test.DUMMY_ACCESS_TOKEN
import noctiluca.test.me
import noctiluca.test.mock.MockTokenDataStore
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.MockTokenDataStoreModule(
    current: AuthorizedUser = me,
) {
    single<TokenDataStore> {
        MockTokenDataStore(listOf(current), DUMMY_ACCESS_TOKEN)
    }
}
