package noctiluca.features.attachment.section

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import noctiluca.features.navigation.component.Back

@Suppress("MagicNumber")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AttachmentPreviewTopAppBar(
    visible: Boolean,
) = AnimatedVisibility(
    visible,
    enter = fadeIn(),
    exit = fadeOut(),
) {
    TopAppBar(
        title = {},
        navigationIcon = { Back() },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(0.6F),
        ),
    )
}