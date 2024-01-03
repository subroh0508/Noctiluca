package noctiluca.features.statusdetail.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import noctiluca.data.authentication.AuthorizedUserRepository
import noctiluca.data.status.StatusRepository
import noctiluca.features.shared.model.MessageHolder
import noctiluca.features.shared.viewmodel.AuthorizedViewModel
import noctiluca.model.StatusId
import noctiluca.model.status.StatusList

class StatusDetailViewModel(
    val id: StatusId,
    private val statusRepository: StatusRepository,
    authorizedUserRepository: AuthorizedUserRepository,
) : AuthorizedViewModel(authorizedUserRepository), ScreenModel {
    private val messageStateFlow: MutableStateFlow<MessageHolder> by lazy {
        MutableStateFlow(MessageHolder("", consumed = true))
    }

    val uiModel: StateFlow<UiModel> by lazy {
        buildUiModel(
            statusRepository.context(id),
            messageStateFlow,
            initialValue = UiModel.Loading,
            started = SharingStarted.WhileSubscribed(5_000),
        ) { statuses, message ->
            UiModel.Loaded(
                statuses = statuses,
                message = message,
            )
        }
    }

    sealed class UiModel {
        data object Loading : UiModel()
        data class Loaded(
            val statuses: StatusList,
            val message: MessageHolder,
        ) : UiModel()

        fun getValue() = (this as? Loaded)?.statuses ?: listOf()
    }
}
