package noctiluca.features.components.atoms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.semantics.Role

fun Modifier.clickable(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    noRipple: Boolean = true,
    onClick: () -> Unit,
): Modifier {
    if (noRipple) {
        return composed {
            clickable(
                remember { MutableInteractionSource() },
                null,
                enabled,
                onClickLabel,
                role,
                onClick,
            )
        }
    }

    return clickable(
        enabled,
        onClickLabel,
        role,
        onClick,
    )
}
