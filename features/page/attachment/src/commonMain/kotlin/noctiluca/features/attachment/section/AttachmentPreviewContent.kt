package noctiluca.features.attachment.section

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import noctiluca.features.navigation.AttachmentParams
import noctiluca.features.shared.components.image.AsyncImage
import noctiluca.features.shared.status.attachment.VideoPlayer
import noctiluca.model.Uri

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun AttachmentPreviewContent(
    attachments: List<Pair<AttachmentParams.Type, Uri>>,
    index: Int,
    onClick: () -> Unit,
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
            modifier = Modifier.fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { onClick() },
                ),
        )
    }
}

@Composable
private fun PageContent(
    type: AttachmentParams.Type,
    url: Uri,
    modifier: Modifier = Modifier,
) = when (type) {
    AttachmentParams.Type.VIDEO -> VideoPlayer(
        url,
        modifier = modifier,
    )

    else -> AsyncImage(
        url,
        modifier = modifier,
    )
}
