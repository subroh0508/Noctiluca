package noctiluca.features.accountdetail.component.topappbar.dropdownmenu.item

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Report
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import noctiluca.features.accountdetail.getString

@Composable
internal fun Report(
    username: String,
    openDialog: MutableState<Boolean>,
    onDismissRequest: () -> Unit,
) = DropdownMenuItem(
    text = { Text(getString().account_detail_menu_report.format(username)) },
    leadingIcon = { Icon(Icons.Default.Report, contentDescription = "Report") },
    onClick = {
        onDismissRequest()
        openDialog.value = true
    },
)

@Composable
internal fun ReportFullScreenDialog(
    username: String,
    openDialog: MutableState<Boolean>,
    report: () -> Unit,
) {
    if (!openDialog.value) {
        return
    }

    // Implement full screen dialog
}
