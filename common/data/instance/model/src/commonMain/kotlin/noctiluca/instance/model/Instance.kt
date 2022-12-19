package noctiluca.instance.model

import noctiluca.model.Uri

data class Instance(
    val domain: String,
    val description: String?,
    val thumbnail: Uri?,
    val languages: List<String>,
    val users: Int,
    val statuses: Int,
    val version: Version?,
) {
    data class Version(
        val major: Int,
        val minor: Int,
        val patch: Int,
    ): Comparable<Version> {
        companion object {
            operator fun invoke(
                version: String,
            ) = version.split(".").let { (major, minor, patch) ->
                Version(major.toInt(), minor.toInt(), patch.toInt())
            }
        }

        override fun compareTo(other: Version) = when {
            major != other.major -> major - other.major
            minor != other.minor -> minor - other.minor
            else -> patch - other.patch
        }

        override fun toString() = listOf(major, minor, patch).joinToString(".")
    }
}
