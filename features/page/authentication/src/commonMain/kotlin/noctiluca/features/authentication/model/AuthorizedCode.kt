package noctiluca.features.authentication.model

import noctiluca.features.navigation.MastodonInstanceDetailParams

expect fun buildAuthorizeResult(
    params: MastodonInstanceDetailParams
): AuthorizeResult?
