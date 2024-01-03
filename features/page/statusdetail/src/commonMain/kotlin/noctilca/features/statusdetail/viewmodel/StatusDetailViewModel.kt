package noctilca.features.statusdetail.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import noctiluca.data.authentication.AuthorizedUserRepository
import noctiluca.features.shared.viewmodel.AuthorizedViewModel
import noctiluca.model.StatusId

class StatusDetailViewModel(
    val id: StatusId,
    authorizedUserRepository: AuthorizedUserRepository,
) : AuthorizedViewModel(authorizedUserRepository), ScreenModel {
}
