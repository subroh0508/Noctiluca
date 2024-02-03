package noctiluca.features.accountdetail.component.topappbar.dropdownmenu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import noctiluca.features.accountdetail.component.topappbar.dropdownmenu.item.Block
import noctiluca.features.accountdetail.component.topappbar.dropdownmenu.item.Mute
import noctiluca.features.accountdetail.component.topappbar.dropdownmenu.item.Report
import noctiluca.features.accountdetail.component.topappbar.dropdownmenu.item.Unmute
import noctiluca.model.accountdetail.Relationships

@Composable
internal fun CommonMenu(
    username: String,
    relationships: Relationships,
    onDismissRequest: () -> Unit,
    openMuteDialog: MutableState<Boolean>,
    openBlockDialog: MutableState<Boolean>,
    openReportDialog: MutableState<Boolean>,
    unmute: () -> Unit,
) {
    Mute(
        username,
        relationships,
        openMuteDialog,
        onDismissRequest = onDismissRequest,
    )
    Unmute(
        username,
        relationships,
        onDismissRequest = onDismissRequest,
        unmute = unmute,
    )
    Block(
        username,
        relationships,
        openBlockDialog,
        onDismissRequest = onDismissRequest,
    )
    Report(
        username,
        openReportDialog,
        onDismissRequest = onDismissRequest,
    )
}
