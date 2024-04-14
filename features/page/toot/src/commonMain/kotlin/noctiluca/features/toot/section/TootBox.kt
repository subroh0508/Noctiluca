package noctiluca.features.toot.section

import androidx.compose.foundation.layout.*
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import noctiluca.features.toot.component.textfield.TootAreaPadding
import noctiluca.features.toot.component.textfield.TootTextArea
import noctiluca.features.toot.model.MediaFile
import noctiluca.features.toot.section.tootbox.BottomBar
import noctiluca.features.toot.section.tootbox.MediaFileGrid
import noctiluca.features.toot.section.tootbox.TootBy
import noctiluca.model.account.Account

@Composable
internal fun TootBox(
    account: Account?,
    content: MutableState<String?>,
    warning: MutableState<String?>,
    files: List<MediaFile>,
    enabled: Boolean,
    onSelectFiles: (List<MediaFile>) -> Unit,
    onClickToot: () -> Unit,
    modifier: Modifier = Modifier,
) = Column(modifier) {
    var isContentWarning by remember { mutableStateOf(false) }

    TootBy(
        account,
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = TootAreaPadding),
    )

    Column(Modifier.fillMaxSize()) {
        Column(Modifier.weight(1F)) {
            TootTextArea(
                content.value ?: "",
                warning.value ?: "",
                isContentWarning = isContentWarning,
                enabled = enabled,
                onChangeContent = { content.value = it },
                onChangeWarningText = { warning.value = it },
            )

            MediaFileGrid(files)
        }

        HorizontalDivider(
            color = DividerDefaults.color,
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
