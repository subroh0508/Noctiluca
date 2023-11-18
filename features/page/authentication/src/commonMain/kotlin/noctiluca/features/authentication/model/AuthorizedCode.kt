package noctiluca.features.authentication.model

import noctiluca.features.navigation.SignInScreen

expect fun buildAuthorizeResult(
    provider: SignInScreen.MastodonInstanceDetail
): AuthorizeResult?
