package noctiluca.features.sigin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.intl.Locale
import noctiluca.features.signin.LocalResources
import noctiluca.features.signin.Resources

@Composable
fun TestComposable(
    content: @Composable () -> Unit,
) = CompositionLocalProvider(
    LocalResources provides Resources(Locale.current.language),
    content = content,
)
