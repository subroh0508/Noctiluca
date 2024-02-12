package noctiluca.features.signin.model

import noctiluca.features.navigation.MastodonInstanceDetailParams

expect fun buildAuthorizeResult(
    params: MastodonInstanceDetailParams
): AuthorizeResult?
