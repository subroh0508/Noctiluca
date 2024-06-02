package noctiluca.features.toot.section

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.components.card.FilledCard
import noctiluca.features.toot.component.textfield.TootAreaPadding
import noctiluca.features.toot.component.textfield.TootTextArea
import noctiluca.features.toot.section.floatingtootcard.TopBar
import noctiluca.features.toot.section.tootbox.BottomBar
import noctiluca.model.media.MediaFile
import noctiluca.model.status.Status

@Composable
internal fun FloatingTootCard(
    content: MutableState<String?>,
    warning: MutableState<String?>,
    visibility: Status.Visibility,
    enabled: Boolean,
    onChangeVisibility: (Status.Visibility) -> Unit,
    onSelectFiles: (List<MediaFile>) -> Unit,
    onClickToot: () -> Unit,
    onClickOpenFullScreen: () -> Unit = {},
    onCloseCard: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var isContentWarning by remember { mutableStateOf(false) }

    FilledCard(
        modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme
                .surfaceVariant
                .copy(alpha = 0.9F),
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 8.dp,
        ),
    ) {
        Spacer(Modifier.height(8.dp))
        TopBar(
            visibility,
            enabled = enabled,
            onChangeVisibility = onChangeVisibility,
            onClickOpenFullScreen = onClickOpenFullScreen,
            onCloseCard = onCloseCard,
        )

        Column(Modifier.fillMaxWidth()) {
            TootTextArea(
                content.value ?: "",
                warning.value ?: "",
                isContentWarning = isContentWarning,
                enabled = enabled,
                borderColor = MaterialTheme.colorScheme.outline,
                onChangeContent = { content.value = it },
                onChangeWarningText = { warning.value = it },
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(horizontal = TootAreaPadding),
            )

            BottomBar(
                content.value ?: "",
                warning.value ?: "",
                isContentWarning = isContentWarning,
                enabled = enabled,
                onSelectFiles = onSelectFiles,
                onToggleContentWarning = {
                    isContentWarning = it

                    if (!it) {
                        warning.value = null
                    }
                },
                onClickToot = onClickToot,
            )
        }
    }
}
