package noctiluca.instance.model

import noctiluca.model.Uri

data class Instance(
    val name: String,
    val domain: String,
    val description: String?,
    val thumbnail: Uri?,
    val languages: List<String>,
    val users: Int,
    val statuses: Int,
    val administrator: Administrator,
    val rules: List<Rule>,
    val version: Version?,
) {
    data class Suggest(
        val domain: String,
        val description: String?,
        val thumbnail: Uri?,
        val version: Version?,
    )

    data class Administrator(
        val screen: String,
        val displayName: String,
        val url: Uri,
        val avatar: Uri,
    )

    data class Rule(
        val id: String,
        val text: String,
    )

    data class Version(
        val major: Int,
        val minor: Int,
        val patch: Int,
    ) : Comparable<Version> {
        companion object {
            @Suppress("MagicNumber")
            operator fun invoke(
                version: String,
            ) = version.split(".")
                .mapNotNull { it.toIntOrNull() }
                .takeIf { it.size == 3 }
                ?.let { (major, minor, patch) -> Version(major, minor, patch) }
        }

        override fun compareTo(other: Version) = when {
            major != other.major -> major - other.major
            minor != other.minor -> minor - other.minor
            else -> patch - other.patch
        }

        override fun toString() = listOf(major, minor, patch).joinToString(".")
    }
}
