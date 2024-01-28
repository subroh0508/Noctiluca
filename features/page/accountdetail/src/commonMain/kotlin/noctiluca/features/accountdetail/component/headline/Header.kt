package noctiluca.features.accountdetail.component.headline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import noctiluca.features.shared.atoms.image.AsyncImage
import noctiluca.model.accountdetail.AccountAttributes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Header(
    attributes: AccountAttributes,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val modifier = Modifier.height(ExpandedTopAppBarHeight)
        .fillMaxWidth()
        .graphicsLayer {
            translationY = calculateTranslationY(scrollBehavior)
        }

    Box(modifier) {
        if (attributes.header == null) {
            Spacer(
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface),
            )
        } else {
            AsyncImage(
                attributes.header,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }

        RelationshipStateChips(
            relationships = attributes.relationships,
            modifier = Modifier.align(Alignment.BottomEnd)
                .graphicsLayer {
                    alpha = 1.0F - scrollBehavior.state.collapsedFraction
                },
        )
    }
}
