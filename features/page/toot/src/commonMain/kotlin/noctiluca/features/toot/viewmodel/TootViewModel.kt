package noctiluca.features.toot.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import noctiluca.data.account.AuthorizedAccountRepository
import noctiluca.data.di.AuthorizedContext
import noctiluca.data.status.MediaRepository
import noctiluca.data.status.StatusRepository
import noctiluca.features.shared.model.MessageHolder
import noctiluca.features.shared.utils.isEnabledToot
import noctiluca.features.shared.viewmodel.AuthorizedViewModel
import noctiluca.features.shared.viewmodel.launchLazy
import noctiluca.features.toot.model.Message
import noctiluca.features.toot.model.TootModel
import noctiluca.model.media.LocalMediaFile
import noctiluca.model.status.Attachment
import noctiluca.model.status.Status

class TootViewModel(
    private val statusRepository: StatusRepository,
    private val mediaRepository: MediaRepository,
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
        files: List<LocalMediaFile>,
    ) {
        files.forEach { file ->
            val job = launchLazy {
                runCatchingWithAuth { mediaRepository.upload(file) }
                    .onSuccess { attachment -> statusTextStateFlow.uploaded(file, attachment) }
                    .onFailure { e -> statusTextStateFlow.failed(file, e) }
            }

            statusTextStateFlow.uploading(file, job)
            job.start()
        }
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
    ) = value.copy(
        content = content,
        warning = warning,
        visibility = visibility,
    )

    private fun MutableStateFlow<TootModel.StatusText>.uploading(
        media: LocalMediaFile,
        job: Job,
    ) {
        val files = value.files
            .toMutableList()
            .filterNot { it.uri == media.original }

        value = value.copy(files = files + TootModel.MediaFile.Uploading(media, job))
    }

    private fun MutableStateFlow<TootModel.StatusText>.uploaded(
        media: LocalMediaFile,
        attachment: Attachment,
    ) {
        val files = value.files
            .toMutableList()
            .filterNot { it.uri == media.original }

        value = value.copy(files = files + TootModel.MediaFile.Uploaded(attachment))
    }

    private fun MutableStateFlow<TootModel.StatusText>.failed(
        media: LocalMediaFile,
        error: Throwable,
    ) {
        val files = value.files
            .toMutableList()
            .filterNot { it.uri == media.original }

        value = value.copy(files = files + TootModel.MediaFile.Failed(media, error))
    }

    private fun reset() {
        message.value = MessageHolder(Message.SENT_NEW_TOOT)
        statusTextStateFlow.value = TootModel.StatusText()
    }

    private fun error(e: Throwable) {
        message.value = MessageHolder(Message.FAILED_SENDING_TOOT, error = e)
    }
}
