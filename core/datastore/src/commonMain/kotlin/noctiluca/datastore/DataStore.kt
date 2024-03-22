package noctiluca.datastore

import androidx.datastore.core.DataMigration
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okio.Path.Companion.toPath

internal fun createDataStore(
    corruptionHandler: ReplaceFileCorruptionHandler<Preferences>? = null,
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
    migrations: List<DataMigration<Preferences>> = listOf(),
    context: Any? = null,
    path: (context: Any?) -> String,
) = PreferenceDataStoreFactory.createWithPath(
    corruptionHandler = corruptionHandler,
    scope = coroutineScope,
    migrations = migrations,
    produceFile = { path(context).toPath() },
)
