package noctiluca.features.toot.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.*
import noctiluca.data.account.AuthorizedAccountRepository
import noctiluca.data.di.AuthorizedContext
import noctiluca.data.status.StatusRepository
import noctiluca.features.shared.model.MessageHolder
import noctiluca.features.shared.utils.isEnabledToot
import noctiluca.features.shared.viewmodel.AuthorizedViewModel
import noctiluca.features.shared.viewmodel.launchLazy
import noctiluca.features.toot.model.MediaFile
import noctiluca.features.toot.model.Message
import noctiluca.features.toot.model.TootModel
import noctiluca.model.status.Status

class TootViewModel(
    private val statusRepository: StatusRepository,
    private val authorizedAccountRepository: AuthorizedAccountRepository,
    context: AuthorizedContext,
) : AuthorizedViewModel(context), ScreenModel {
    private val statusTextStateFlow by lazy { MutableStateFlow(TootModel.StatusText()) }
    private val message by lazy { MutableStateFlow(MessageHolder<Message>()) }

    val uiModel: StateFlow<TootModel> by lazy {
        buildUiModel(
            context.state,
            authorizedAccountRepository.all(),
            statusTextStateFlow,
            message,
            initialValue = TootModel(),
            started = SharingStarted.WhileSubscribed(5_000),
        ) { authorizedEventState, accounts, statusText, message ->
            TootModel(
                authorizedEventState,
                accounts,
                statusText,
                message,
            )
        }
    }

    fun onChange(
        content: String?,
        warning: String?,
    ) {
        statusTextStateFlow.value = statusTextStateFlow.copy(
            content = content,
            warning = warning,
        )
    }

    fun onChangeVisibility(
        visibility: Status.Visibility,
    ) {
        statusTextStateFlow.value = statusTextStateFlow.copy(visibility = visibility)
    }

    fun onSelectFiles(
        files: List<MediaFile>,
    ) {
        statusTextStateFlow.value = statusTextStateFlow.copy(files = files)
    }

    fun onRemoveFile(
        index: Int,
    ) {
        statusTextStateFlow.value = statusTextStateFlow.value.copy(
            files = statusTextStateFlow.value.files.toMutableList().apply { removeAt(index) },
        )
    }

    fun toot() {
        val content = uiModel.value.statusText.content ?: return
        if (!isEnabledToot(content) || uiModel.value.message.text == Message.SENDING) {
            return
        }

        val job = launchLazy {
            runCatchingWithAuth {
                statusRepository.new(
                    content,
                    uiModel.value.statusText.warning,
                    uiModel.value.statusText.visibility,
                )
            }
                .onSuccess { reset() }
                .onFailure { e -> error(e) }
        }

        message.value = MessageHolder(Message.SENDING, job = job)
        job.start()
    }

    private fun MutableStateFlow<TootModel.StatusText>.copy(
        content: String? = value.content,
        warning: String? = value.warning,
        visibility: Status.Visibility = value.visibility,
        files: List<MediaFile> = value.files,
    ) = value.copy(
        content = content,
        warning = warning,
        visibility = visibility,
        files = files,
    )

    private fun reset() {
        message.value = MessageHolder(Message.SENT_NEW_TOOT)
        statusTextStateFlow.value = TootModel.StatusText()
    }

    private fun error(e: Throwable) {
        message.value = MessageHolder(Message.FAILED_SENDING_TOOT, error = e)
    }
}
