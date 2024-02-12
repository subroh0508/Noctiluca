package noctiluca.features.signin.component.tab.extendeddescription

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.shared.atoms.card.FilledCard
import noctiluca.features.shared.atoms.list.SectionPadding
import noctiluca.features.shared.atoms.text.HtmlText
import noctiluca.model.authentication.Instance

@Suppress("FunctionNaming")
internal fun LazyListScope.InstanceExtendedDescriptionTab(
    instance: Instance?,
) {
    val extendedDescription = instance?.extendedDescription ?: return

    item {
        FilledCard(Modifier.padding(SectionPadding)) {
            Box(Modifier.padding(16.dp)) {
                HtmlText(extendedDescription)
            }
        }
    }
}
