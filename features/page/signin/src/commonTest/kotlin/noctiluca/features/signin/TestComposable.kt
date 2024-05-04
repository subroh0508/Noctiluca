package noctiluca.features.signin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.intl.Locale

@Composable
fun TestComposable(
    locale: Locale = Locale("ja"),
    content: @Composable () -> Unit,
) = CompositionLocalProvider(
    LocalResources provides Resources(locale.language),
    content = content,
)
