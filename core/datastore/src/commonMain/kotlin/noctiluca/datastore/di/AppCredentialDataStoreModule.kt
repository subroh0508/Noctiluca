package noctiluca.datastore.di

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.module.Module

@Suppress("FunctionName")
expect fun Module.AppCredentialDataStoreModule()

internal expect fun createAppCredentialDataStore(
    migrations: List<DataMigration<Preferences>> = listOf(),
    context: Any? = null,
): DataStore<Preferences>
