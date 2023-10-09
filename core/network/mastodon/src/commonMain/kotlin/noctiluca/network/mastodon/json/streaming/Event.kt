package noctiluca.network.mastodon.json.streaming

/**
 *  ref. https://docs.joinmastodon.org/methods/streaming/#events
 */

enum class Event(val value: String) {
    UPDATE("update"),
    DELETE("delete"),
    NOTIFICATION("notification"),
    FILTERS_CHANGED("filters_changed"),
    CONVERSATION("conversation"),
    ANNOUNCEMENT("announcement"),
    ANNOUNCEMENT_REACTION("announcement.reaction"),
    ANNOUNCEMENT_DELETE("announcement.delete"),
    STATUS_UPDATE("status.update"),
    ENCRYPTED_MESSAGE("encrypted_message");

    companion object {
        fun findEvent(event: String) = values().find {
            it.value == event
        } ?: throw IllegalArgumentException("Not found: $event")
    }
}
