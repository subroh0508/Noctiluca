package noctiluca.features.toot.model

import kotlinx.coroutines.Job
import noctiluca.features.shared.model.MessageHolder
import noctiluca.model.AuthorizeEventState
import noctiluca.model.Uri
import noctiluca.model.account.Account
import noctiluca.model.media.LocalMediaFile
import noctiluca.model.status.Attachment
import noctiluca.model.status.Status

data class TootModel(
    val current: Account? = null,
    val statusText: StatusText = StatusText(),
    val message: MessageHolder<Message> = MessageHolder(),
) {
    constructor(
        state: AuthorizeEventState?,
        accounts: List<Account>,
        statusText: StatusText,
        message: MessageHolder<Message>,
    ) : this(
        current = accounts.find { it.id == state?.user?.id },
        statusText = statusText,
        message = message,
    )

    val isSendingSuccessful
        get() = message.text in listOf(
            Message.SENT_NEW_TOOT,
            Message.SENT_EDITED_TOOT,
        )

    data class StatusText(
        val content: String? = null,
        val warning: String? = null,
        val visibility: Status.Visibility = Status.Visibility.PUBLIC,
        val sensitive: Boolean = false,
        val files: List<MediaFile> = listOf(),
    ) {
        val mediaIds = files.mapNotNull { (it as? MediaFile.Uploaded)?.attachment?.id }
    }

    sealed class MediaFile {
        abstract val uri: Uri
        abstract val previewUri: Uri?

        data class Uploading(val file: LocalMediaFile, val job: Job) : MediaFile() {
            override val uri get() = file.original
            override val previewUri get() = file.preview
        }

        data class Uploaded(val attachment: Attachment) : MediaFile() {
            override val uri get() = attachment.url
            override val previewUri get() = attachment.previewUrl
        }

        data class Failed(val file: LocalMediaFile, val error: Throwable) : MediaFile() {
            override val uri get() = file.original
            override val previewUri get() = file.preview
        }
    }
}
