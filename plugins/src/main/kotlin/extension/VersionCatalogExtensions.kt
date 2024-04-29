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
internal val VersionCatalog.ktorClientResources: Provider<MinimalExternalModuleDependency> get() = getLibrary("ktor-client-resources")
internal val VersionCatalog.ktorClientMock: Provider<MinimalExternalModuleDependency> get() = getLibrary("ktor-client-mock")

internal val VersionCatalog.voyagerNavigator: Provider<MinimalExternalModuleDependency> get() = getLibrary("voyager-navigator")
internal val VersionCatalog.voyagerTransitions: Provider<MinimalExternalModuleDependency> get() = getLibrary("voyager-transitions")
internal val VersionCatalog.voyagerKoin: Provider<MinimalExternalModuleDependency> get() = getLibrary("voyager-koin")

internal val VersionCatalog.koinCore: Provider<MinimalExternalModuleDependency> get() = getLibrary("koin-core")
internal val VersionCatalog.koinAndroid: Provider<MinimalExternalModuleDependency> get() = getLibrary("koin-android")

internal val VersionCatalog.androidxActivities: Provider<ExternalModuleDependencyBundle> get() = getBundle("androidx-activities")
internal val VersionCatalog.androidxComposeMaterial3: Provider<MinimalExternalModuleDependency> get() = getLibrary("androidx-compose-material3")
internal val VersionCatalog.androidxComposeUiTestJunit4Android: Provider<MinimalExternalModuleDependency>
    get() = getLibrary(
        "androidx-compose-ui-test-junit4-android"
    )
internal val VersionCatalog.androidxComposeUiTestManifest: Provider<MinimalExternalModuleDependency>
    get() = getLibrary(
        "androidx-compose-ui-test-manifest"
    )
internal val VersionCatalog.kotestAssertionsCore: Provider<MinimalExternalModuleDependency> get() = getLibrary("kotest-assertions-core")
internal val VersionCatalog.kotestFrameworkEngine: Provider<MinimalExternalModuleDependency> get() = getLibrary("kotest-framework-engine")
internal val VersionCatalog.kotestFrameworkDataTest: Provider<MinimalExternalModuleDependency>
    get() = getLibrary(
        "kotest-framework-datatest"
    )
internal val VersionCatalog.kotestRunnerJunit5: Provider<MinimalExternalModuleDependency> get() = getLibrary("kotest-runner-junit5")

internal val VersionCatalog.junit: Provider<MinimalExternalModuleDependency> get() = getLibrary("junit-core")
internal val VersionCatalog.junitVintage: Provider<MinimalExternalModuleDependency>
    get() = getLibrary(
        "junit-vintage"
    )
internal val VersionCatalog.androidxTestCore: Provider<MinimalExternalModuleDependency>
    get() = getLibrary(
        "androidx-test-core"
    )
internal val VersionCatalog.androidxTestRunner: Provider<MinimalExternalModuleDependency>
    get() = getLibrary(
        "androidx-test-runner"
    )
internal val VersionCatalog.androidxTestJunit: Provider<MinimalExternalModuleDependency>
    get() = getLibrary(
        "androidx-test-junit"
    )
internal val VersionCatalog.robolectric: Provider<MinimalExternalModuleDependency>
    get() = getLibrary(
        "robolectric"
    )

internal val VersionCatalog.androidDesugarjdk: Provider<MinimalExternalModuleDependency> get() = getLibrary("android-desugarjdk")

internal val Project.libs: VersionCatalog get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

private fun VersionCatalog.getLibrary(library: String) = findLibrary(library).get()
private fun VersionCatalog.getBundle(bundle: String) = findBundle(bundle).get()
