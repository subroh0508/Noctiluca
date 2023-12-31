package noctiluca.model.timeline

sealed interface TimelineId

data object GlobalTimelineId : TimelineId
data object LocalTimelineId : TimelineId
data object HomeTimelineId : TimelineId

@JvmInline
value class HashtagTimelineId(val hashtag: String) : TimelineId

@JvmInline
value class ListTimelineId(val listId: String) : TimelineId
