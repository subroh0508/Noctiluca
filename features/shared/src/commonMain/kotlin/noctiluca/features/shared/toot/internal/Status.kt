package noctiluca.features.shared.toot.internal

import androidx.compose.runtime.Composable
import noctiluca.features.components.getCommonString
import noctiluca.model.status.Status

@Composable
internal fun Status.Visibility.label() = when (this) {
    Status.Visibility.PUBLIC -> getCommonString().visibility_public
    Status.Visibility.UNLISTED -> getCommonString().visibility_unlisted
    Status.Visibility.PRIVATE -> getCommonString().visibility_private
    Status.Visibility.DIRECT -> getCommonString().visibility_direct
}

@Composable
internal fun Status.Visibility.supportText() = when (this) {
    Status.Visibility.PUBLIC -> getCommonString().visibility_public_support_text
    Status.Visibility.UNLISTED -> getCommonString().visibility_unlisted_support_text
    Status.Visibility.PRIVATE -> getCommonString().visibility_private_support_text
    Status.Visibility.DIRECT -> getCommonString().visibility_direct_support_text
}
