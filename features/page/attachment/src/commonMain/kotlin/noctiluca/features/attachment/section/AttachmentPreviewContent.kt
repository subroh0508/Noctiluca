package noctiluca.features.attachment.section

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import noctiluca.features.attachment.component.VideoPlayer
import noctiluca.features.navigation.AttachmentParams
import noctiluca.features.shared.components.image.AsyncImage
import noctiluca.model.Uri

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun AttachmentPreviewContent(
    attachments: List<Pair<AttachmentParams.Type, Uri>>,
    index: Int,
    isVisibleTopAppBar: Boolean,
    onToggleTopAppBar: (Boolean) -> Unit,
) {
    val pagerState = rememberPagerState(
        index,
        pageCount = { attachments.size },
    )

    HorizontalPager(
        pagerState,
        modifier = Modifier.fillMaxSize(),
    ) { page ->
        val (type, url) = attachments[page]

        PageContent(
            type,
            url,
            isVisibleTopAppBar,
            onToggleTopAppBar = onToggleTopAppBar,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun PageContent(
    type: AttachmentParams.Type,
    url: Uri,
    isVisibleTopAppBar: Boolean,
    onToggleTopAppBar: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) = when (type) {
    AttachmentParams.Type.VIDEO -> VideoPlayer(
        url,
        isVisibleTopAppBar,
        onChangeControllerVisibility = onToggleTopAppBar,
        modifier = modifier,
    )

    else -> AsyncImage(
        url,
        modifier = modifier,
    )
}
