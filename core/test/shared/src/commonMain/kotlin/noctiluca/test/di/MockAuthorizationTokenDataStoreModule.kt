package noctiluca.test.di

import noctiluca.datastore.AuthorizationTokenDataStore
import noctiluca.model.AuthorizedUser
import noctiluca.test.DUMMY_ACCESS_TOKEN
import noctiluca.test.me
import noctiluca.test.mock.MockAuthorizationTokenDataStore
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.MockAuthorizationTokenDataStoreModule(
    current: AuthorizedUser = me,
) {
    single<AuthorizationTokenDataStore> {
        MockAuthorizationTokenDataStore(listOf(current), DUMMY_ACCESS_TOKEN)
    }
}
