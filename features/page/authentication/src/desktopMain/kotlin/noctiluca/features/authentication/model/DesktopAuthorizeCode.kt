package noctiluca.features.authentication.model

import noctiluca.features.navigation.SignInScreen

actual fun buildAuthorizeResult(
    provider: SignInScreen.MastodonInstanceDetail
): AuthorizeResult? = null
