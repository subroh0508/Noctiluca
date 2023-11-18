package noctiluca.test.di

import noctiluca.datastore.AuthenticationTokenDataStore
import noctiluca.model.AuthorizedUser
import noctiluca.test.DUMMY_ACCESS_TOKEN
import noctiluca.test.me
import noctiluca.test.mock.MockAuthenticationTokenDataStore
import org.koin.core.module.Module

@Suppress("FunctionName")
fun Module.MockAuthenticationTokenDataStoreModule(
    current: AuthorizedUser = me,
) {
    single<AuthenticationTokenDataStore> {
        MockAuthenticationTokenDataStore(listOf(current), DUMMY_ACCESS_TOKEN)
    }
}
