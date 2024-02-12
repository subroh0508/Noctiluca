package noctiluca.features.signin.section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import noctiluca.features.signin.component.InstanceDescription
import noctiluca.features.signin.component.InstanceName
import noctiluca.features.signin.component.InstanceThumbnail
import noctiluca.model.Uri
import noctiluca.model.signin.Instance

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
    item {
        Column(modifier = Modifier.fillMaxWidth()) {
            InstanceThumbnail(thumbnail, horizontalPadding)
            InstanceName(name, domain, horizontalPadding)
            InstanceDescription(description, horizontalPadding)
        }
    }
    item { tabs() }
}
