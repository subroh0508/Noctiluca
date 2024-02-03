package noctiluca.features.accountdetail.model

import noctiluca.features.shared.model.LoadState
import noctiluca.model.accountdetail.Relationships

data class RelationshipsStateModel(
    val relationships: Relationships = Relationships.NONE,
    val state: LoadState = LoadState.Initial,
)
