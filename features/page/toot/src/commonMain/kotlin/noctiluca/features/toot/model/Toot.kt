package noctiluca.features.toot.model

internal const val MAX_CONTENT_LENGTH = 500

internal fun isEnabledToot(
    content: String?,
) = (content?.length ?: 0) in 1..MAX_CONTENT_LENGTH
