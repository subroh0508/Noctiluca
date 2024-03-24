package noctiluca.datastore.di

import org.koin.core.module.Module

@Suppress("FunctionName")
expect fun Module.AppCredentialDataStoreModule()

internal fun preferencesFileName(fileName: String) = "$fileName.preferences_pb"
