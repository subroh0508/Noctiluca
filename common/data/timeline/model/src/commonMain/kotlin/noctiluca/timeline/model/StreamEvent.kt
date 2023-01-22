package noctiluca.timeline.model

import noctiluca.model.StatusId
import noctiluca.status.model.Status

sealed class StreamEvent {
    data class Updated(val status: Status) : StreamEvent()
    data class Deleted(val id: StatusId) : StreamEvent()
    data class StatusEdited(val status: Status) : StreamEvent()

    // data class UpdatedConversation : StreamEvent()
    // data class UpdatedNotification : StreamEvent()
    // object FiltersChanged : StreamEvent()
    // data class UpdatedAnnouncement : StreamEvent()
    // data class DeletedAnnouncement : StreamEvent()
}
