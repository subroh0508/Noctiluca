package extension

import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType

internal val VersionCatalog.detektFormatting: Provider<MinimalExternalModuleDependency> get() = getLibrary("detekt-formatting")

internal val VersionCatalog.coroutinesCore: Provider<MinimalExternalModuleDependency> get() = getLibrary("kotlinx-coroutines-core")

internal val VersionCatalog.ktorSerializationKotlinxJson: Provider<MinimalExternalModuleDependency> get() = getLibrary("ktor-serialization-kotlinx-json")
internal val VersionCatalog.ktorClientMock: Provider<MinimalExternalModuleDependency> get() = getLibrary("ktor-client-mock")

internal val VersionCatalog.koinCore: Provider<MinimalExternalModuleDependency> get() = getLibrary("koin-core")
internal val VersionCatalog.koinAndroid: Provider<MinimalExternalModuleDependency> get() = getLibrary("koin-android")

internal val VersionCatalog.androidxActivities: Provider<ExternalModuleDependencyBundle> get() = getBundle("androidx-activities")

internal val VersionCatalog.kotestAssertionsCore: Provider<MinimalExternalModuleDependency> get() = getLibrary("kotest-assertions-core")
internal val VersionCatalog.kotestFrameworkEngine: Provider<MinimalExternalModuleDependency> get() = getLibrary("kotest-framework-engine")
internal val VersionCatalog.kotestRunnerJunit5: Provider<MinimalExternalModuleDependency> get() = getLibrary("kotest-runner-junit5")

internal val Project.libs: VersionCatalog get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

private fun VersionCatalog.getLibrary(library: String) = findLibrary(library).get()
private fun VersionCatalog.getBundle(bundle: String) = findBundle(bundle).get()
