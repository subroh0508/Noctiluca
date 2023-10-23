package noctiluca.features.authentication.organisms.tab.extendeddescription

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.components.atoms.card.FilledCard
import noctiluca.features.components.atoms.list.SectionPadding
import noctiluca.features.components.atoms.text.HtmlText
import noctiluca.model.authentication.Instance

@Composable
internal fun InstanceExtendedDescriptionTab(
    instance: Instance,
) {
    val extendedDescription = instance.extendedDescription ?: return

    FilledCard(Modifier.padding(SectionPadding)) {
        Box(Modifier.padding(16.dp)) {
            HtmlText(extendedDescription)
        }
    }
}
