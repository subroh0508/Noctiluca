package noctiluca.features.shared.status.attachment

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
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
            ImagePager(previewUrls, index)
            DialogTopBar(onDismissRequest = onDismissRequest)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogTopBar(
    onDismissRequest: () -> Unit,
) = TopAppBar(
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ImagePager(
    previewUrls: List<Uri>,
    initialIndex: Int,
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
            modifier = Modifier.fillMaxSize(),
        )
    }
}
