package extension

import org.gradle.api.artifacts.VersionCatalog

private fun VersionCatalog.getLibrary(library: String) = findLibrary(library).get()
