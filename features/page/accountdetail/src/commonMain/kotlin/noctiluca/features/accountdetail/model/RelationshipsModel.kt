package noctiluca.features.accountdetail.model

import noctiluca.features.shared.model.LoadState
import noctiluca.model.accountdetail.Relationships

data class RelationshipsModel(
    val relationships: Relationships = Relationships.NONE,
    val state: LoadState = LoadState.Initial,
)
