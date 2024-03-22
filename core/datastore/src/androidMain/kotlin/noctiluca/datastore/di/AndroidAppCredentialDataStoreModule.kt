package noctiluca.datastore.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import noctiluca.datastore.AppCredentialDataStore
import noctiluca.datastore.createDataStore
import noctiluca.datastore.internal.AppCredentialDataStoreImpl
import noctiluca.datastore.internal.preferenceDataStoreFile
import org.koin.core.module.Module

@Suppress("FunctionName")
actual fun Module.AppCredentialDataStoreModule() {
    single<AppCredentialDataStore> {
        AppCredentialDataStoreImpl(
            createAppCredentialDataStore(
                context = get<Application>(),
            ),
        )
    }
}

internal actual fun createAppCredentialDataStore(
    migrations: List<DataMigration<Preferences>>,
    context: Any?,
): DataStore<Preferences> = createDataStore(
    context = context,
    path = {
        it ?: throw IllegalArgumentException("Context is required")

        preferenceDataStoreFile(
            dir = (it as Context).filesDir,
            fileName = AppCredentialDataStore::class.simpleName ?: "",
        ).path
    },
)
