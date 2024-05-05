package noctiluca.test.shared.di

import noctiluca.datastore.AuthorizationTokenDataStore
import noctiluca.model.AuthorizedUser
import noctiluca.test.shared.DUMMY_ACCESS_TOKEN
import noctiluca.test.shared.me
import noctiluca.test.shared.mock.MockAuthorizationTokenDataStore
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.MockAuthorizationTokenDataStoreModule(
    current: AuthorizedUser = me,
) {
    single<AuthorizationTokenDataStore> {
        MockAuthorizationTokenDataStore(listOf(current), DUMMY_ACCESS_TOKEN)
    }
}
