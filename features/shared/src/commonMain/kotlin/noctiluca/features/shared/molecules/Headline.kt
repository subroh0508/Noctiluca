package noctiluca.features.shared.molecules

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.atoms.text.HeadlineSmall

@Composable
fun HeadlineWithProgress(
    title: String,
    loading: Boolean,
    modifier: Modifier = Modifier,
) {
    if (loading) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    } else {
        Spacer(Modifier.height(4.dp))
    }

    HeadlineSmall(title, modifier)
    Spacer(Modifier.height(32.dp))
}