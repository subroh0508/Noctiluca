package noctiluca.features.accountdetail.model

import noctiluca.features.shared.model.LoadState
import noctiluca.model.accountdetail.AccountAttributes

data class AttributesModel(
    val attributes: AccountAttributes? = null,
    val state: LoadState = LoadState.Initial,
)
