package noctiluca.datastore.di

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import noctiluca.datastore.AppCredentialDataStore
import noctiluca.datastore.createDataStore
import noctiluca.datastore.internal.AppCredentialDataStoreImpl
import org.koin.core.module.Module

@Suppress("FunctionName")
actual fun Module.AppCredentialDataStoreModule() {
    single<AppCredentialDataStore> {
        AppCredentialDataStoreImpl(
            createAppCredentialDataStore(context = null),
        )
    }
}

actual fun createAppCredentialDataStore(
    migrations: List<DataMigration<Preferences>>,
    context: Any?,
): DataStore<Preferences> = createDataStore(
    context = context,
    path = { AppCredentialDataStore::class.simpleName ?: "" },
)
