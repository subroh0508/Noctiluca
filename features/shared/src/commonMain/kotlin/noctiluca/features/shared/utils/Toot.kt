package noctiluca.features.shared.utils

internal const val MAX_CONTENT_LENGTH = 500

fun isEnabledToot(
    content: String?,
) = (content?.length ?: 0) in 1..MAX_CONTENT_LENGTH
