package noctiluca.features.statusdetail.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import noctiluca.data.di.AuthorizedContext
import noctiluca.data.status.StatusRepository
import noctiluca.features.shared.model.MessageHolder
import noctiluca.features.shared.viewmodel.AuthorizedViewModel
import noctiluca.features.statusdetail.model.Message
import noctiluca.features.statusdetail.model.StatusDetailModel
import noctiluca.model.StatusId

class StatusDetailViewModel(
    val id: StatusId,
    private val statusRepository: StatusRepository,
    context: AuthorizedContext,
) : AuthorizedViewModel(context), ScreenModel {
    private val messageStateFlow: MutableStateFlow<MessageHolder<Message>> by lazy {
        MutableStateFlow(MessageHolder())
    }

    val uiModel: StateFlow<StatusDetailModel> by lazy {
        buildUiModel(
            statusRepository.context(id),
            messageStateFlow,
            initialValue = StatusDetailModel(loading = true),
            started = SharingStarted.WhileSubscribed(5_000),
        ) { statuses, message ->
            StatusDetailModel(
                statuses = statuses,
                message = message,
            )
        }
    }
}
