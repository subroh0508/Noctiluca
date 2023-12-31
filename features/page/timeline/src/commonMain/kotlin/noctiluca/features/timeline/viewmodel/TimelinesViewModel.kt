package noctiluca.features.timeline.viewmodel

import androidx.compose.runtime.*
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import noctiluca.data.account.AuthorizedAccountRepository
import noctiluca.data.authentication.AuthorizedUserRepository
import noctiluca.data.timeline.TimelineRepository
import noctiluca.features.shared.viewmodel.AuthorizedViewModel
import noctiluca.features.shared.viewmodel.launch
import noctiluca.features.shared.viewmodel.viewModelScope
import noctiluca.features.timeline.model.CurrentAuthorizedAccount
import noctiluca.model.account.Account
import noctiluca.model.status.Status
import noctiluca.model.timeline.LocalTimelineId
import noctiluca.model.timeline.StreamEvent
import noctiluca.model.timeline.Timeline
import noctiluca.model.timeline.TimelineId
import noctiluca.timeline.domain.model.StatusAction
import noctiluca.timeline.domain.usecase.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

@Suppress("TooManyFunctions", "LongParameterList")
class TimelinesViewModel(
    private val executeStatusActionUseCase: ExecuteStatusActionUseCase,
    private val authorizedAccountRepository: AuthorizedAccountRepository,
    private val timelineRepository: TimelineRepository,
    authorizedUserRepository: AuthorizedUserRepository,
) : AuthorizedViewModel(authorizedUserRepository), ScreenModel {
    private val foregroundIdStateFlow by lazy { MutableStateFlow<TimelineId>(LocalTimelineId) }

    val uiModel: StateFlow<UiModel> by lazy {
        combine(
            authorizedAccountRepository.current(),
            authorizedAccountRepository.others(),
            timelineRepository.buildStream(),
            foregroundIdStateFlow,
        ) { current, others, timelines, timelineId ->
            UiModel(
                account = CurrentAuthorizedAccount(current, others),
                timelines = timelines.map { (id, timeline) ->
                    id to TimelineState(
                        timeline = timeline,
                        foreground = timelineId == id,
                    )
                }.toMap(),
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = UiModel(),
            )
    }

    fun switch(account: Account) {
        launch {
            authorizedAccountRepository.switch(account.id)
            timelineRepository.close()
            reopen()
        }
    }

    fun setForeground(index: Int) {
        foregroundIdStateFlow.value = uiModel.value.findTimelineId(index)
    }

    fun scrolledToTop(timelineId: TimelineId) = Unit

    fun subscribeAll() {
        launch {
            runCatchingWithAuth { timelineRepository.start() }
        }
    }

    fun load(timeline: Timeline) {
        launch {
            // runCatchingWithAuth { timelineRepository.load(timeline) }
        }
    }

    fun favourite(timeline: Timeline, status: Status) = execute(timeline, status, StatusAction.FAVOURITE)
    fun boost(timeline: Timeline, status: Status) = execute(timeline, status, StatusAction.BOOST)
    fun bookmark(timeline: Timeline, status: Status) = execute(timeline, status, StatusAction.BOOKMARK)

    fun clear() {
        launch {
            timelineRepository.close()
        }
    }

    private fun execute(timeline: Timeline, status: Status, action: StatusAction) = Unit

    data class UiModel(
        val account: CurrentAuthorizedAccount = CurrentAuthorizedAccount(),
        val timelines: Map<TimelineId, TimelineState> = mapOf(),
    ) {
        val foreground get() = timelines.values.find { it.foreground }
        val currentTabIndex get() = timelines.values.indexOfFirst { it.foreground }

        fun toTimelineList() = timelines.values.toList()
        fun findTimelineId(index: Int) = timelines.keys.toList()[index]
    }

    data class TimelineState(
        val timeline: Timeline,
        val jobs: List<Job> = listOf(),
        val latestEvent: StreamEvent? = null,
        val scrollToTop: Boolean = false,
        val foreground: Boolean = false,
    )

    companion object Provider {
        @Composable
        operator fun invoke(
            component: KoinComponent,
        ): TimelinesViewModel {
            return remember {
                TimelinesViewModel(
                    component.get(),
                    component.get(),
                    component.get(),
                    component.get(),
                )
            }
        }
    }
}
