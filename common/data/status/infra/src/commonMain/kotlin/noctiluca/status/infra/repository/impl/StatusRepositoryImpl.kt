package noctiluca.status.infra.repository.impl

import noctiluca.api.mastodon.MastodonApiV1
import noctiluca.repository.TokenProvider
import noctiluca.status.infra.repository.StatusRepository

internal class StatusRepositoryImpl(
    private val api: MastodonApiV1,
    private val tokenProvider: TokenProvider,
) : StatusRepository
