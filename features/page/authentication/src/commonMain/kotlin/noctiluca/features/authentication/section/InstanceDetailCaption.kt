package noctiluca.features.authentication.section

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import noctiluca.features.authentication.component.InstanceDescription
import noctiluca.features.authentication.component.InstanceName
import noctiluca.features.authentication.component.InstanceThumbnail
import noctiluca.model.Uri
import noctiluca.model.authentication.Instance

@Suppress("FunctionNaming")
internal fun LazyListScope.InstanceDetailCaption(
    instance: Instance?,
    tabs: @Composable () -> Unit,
    horizontalPadding: Dp,
) {
    instance ?: return

    InstanceDetailCaption(
        instance.thumbnail,
        instance.name,
        instance.domain,
        instance.description,
        tabs,
        horizontalPadding,
    )
}

@Suppress("FunctionNaming")
internal fun LazyListScope.InstanceDetailCaption(
    thumbnail: Uri?,
    name: String,
    domain: String,
    description: String?,
    tabs: @Composable () -> Unit,
    horizontalPadding: Dp,
) {
    item { InstanceThumbnail(thumbnail, horizontalPadding) }
    item { InstanceName(name, domain, horizontalPadding) }
    item { InstanceDescription(description, horizontalPadding) }
    item { tabs() }
}
