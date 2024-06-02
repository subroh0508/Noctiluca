package noctiluca.features.toot.model

import noctiluca.features.shared.model.MessageHolder
import noctiluca.model.AuthorizeEventState
import noctiluca.model.account.Account
import noctiluca.model.media.LocalMediaFile
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
        val files: List<LocalMediaFile> = listOf(),
    )
}
