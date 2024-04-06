package noctiluca.features.shared.status.attachment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import noctiluca.features.shared.components.image.AsyncImage
import noctiluca.model.Uri

@Composable
internal fun ImagePreviewDialog(
    previewUrls: List<Uri>,
    index: Int,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
        ),
        onDismissRequest = onDismissRequest,
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
        ) {
            DialogContent(
                previewUrls,
                index,
                onDismissRequest = onDismissRequest,
            )
        }
    }
}

@Composable
private fun DialogContent(
    previewUrls: List<Uri>,
    initialIndex: Int,
    onDismissRequest: () -> Unit,
) {
    var visibleTopAppBar by remember { mutableStateOf(true) }

    ImagePager(
        previewUrls,
        initialIndex,
        onClick = { visibleTopAppBar = !visibleTopAppBar },
    )
    DialogTopBar(
        visibleTopAppBar,
        onDismissRequest = onDismissRequest,
    )
}

@Suppress("MagicNumber")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogTopBar(
    visible: Boolean,
    onDismissRequest: () -> Unit,
) = AnimatedVisibility(
    visible,
    enter = fadeIn(),
    exit = fadeOut(),
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(
                onClick = onDismissRequest,
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(0.6F),
        ),
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ImagePager(
    previewUrls: List<Uri>,
    initialIndex: Int,
    onClick: () -> Unit,
) {
    val pagerState = rememberPagerState(
        initialIndex,
        pageCount = { previewUrls.size },
    )

    HorizontalPager(
        pagerState,
        modifier = Modifier.fillMaxSize(),
    ) { page ->
        AsyncImage(
            previewUrls[page],
            modifier = Modifier.fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { onClick() },
                ),
        )
    }
}
