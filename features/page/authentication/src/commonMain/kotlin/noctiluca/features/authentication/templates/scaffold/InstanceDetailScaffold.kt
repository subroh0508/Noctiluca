package noctiluca.features.authentication.templates.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.organisms.tab.InstanceDetailTabs
import noctiluca.features.authentication.organisms.tab.extendeddescription.InstanceExtendedDescriptionTab
import noctiluca.features.authentication.organisms.tab.info.InstanceInformationTab
import noctiluca.features.authentication.organisms.tab.rememberTabbedInstanceDetailState
import noctiluca.features.authentication.state.Instances
import noctiluca.features.components.atoms.image.AsyncImage
import noctiluca.features.components.atoms.text.HtmlText
import noctiluca.features.components.molecules.scaffold.HeadlineText
import noctiluca.features.components.molecules.scaffold.HeadlineTopAppBar
import noctiluca.features.components.molecules.scaffold.HeadlinedScaffold
import noctiluca.instance.model.Instance
import noctiluca.model.Uri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InstanceDetailScaffold(
    instance: Instance,
    onBackPressed: () -> Unit,
) {
    val statusesScrollState = rememberTabbedInstanceDetailState(instance)

    HeadlinedScaffold(
        statusesScrollState.lazyListState,
        tabComposeIndex = 3,
        topAppBar = { scrollBehavior ->
            HeadlineTopAppBar(
                title = {
                    HeadlineText(
                        instance.name,
                        instance.domain,
                        statusesScrollState.lazyListState.firstVisibleItemIndex > 1,
                    )
                },
                onBackPressed = onBackPressed,
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = { horizontalPadding -> ActionButtons(horizontalPadding) },
        tabs = { InstanceDetailTabs(statusesScrollState) },
    ) { tabs, horizontalPadding ->
        item { InstanceThumbnail(instance.thumbnail, horizontalPadding) }
        item { InstanceName(instance, horizontalPadding) }
        item { InstanceDescription(instance, horizontalPadding) }
        item { tabs() }

        item {
            when (statusesScrollState.tab) {
                Instances.Tab.INFO -> InstanceInformationTab(instance)
                Instances.Tab.EXTENDED_DESCRIPTION -> InstanceExtendedDescriptionTab(instance)
                Instances.Tab.LOCAL_TIMELINE -> Unit
            }
        }
    }
}

@Composable
private fun InstanceThumbnail(
    thumbnail: Uri?,
    horizontalPadding: Dp,
) = Box(
    Modifier.padding(
        top = 8.dp,
        start = horizontalPadding,
        end = horizontalPadding,
    )
) {
    AsyncImage(
        thumbnail,
        contentScale = ContentScale.FillWidth,
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
    )
}

@Composable
private fun InstanceName(
    instance: Instance,
    horizontalPadding: Dp,
) = Column(
    modifier = Modifier.fillMaxWidth()
        .padding(
            vertical = 16.dp,
            horizontal = horizontalPadding,
        ),
) {
    Text(
        instance.name,
        style = MaterialTheme.typography.headlineSmall,
    )

    Text(
        instance.domain,
        color = MaterialTheme.colorScheme.outline,
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Composable
private fun InstanceDescription(
    instance: Instance,
    horizontalPadding: Dp,
) = HtmlText(
    instance.description ?: "",
    style = MaterialTheme.typography.bodyLarge,
    modifier = Modifier.fillMaxWidth()
        .padding(
            bottom = 16.dp,
            start = horizontalPadding,
            end = horizontalPadding,
        ),
)

@Composable
private fun BoxScope.ActionButtons(
    horizontalPadding: Dp,
) = Column(
    modifier = Modifier.fillMaxWidth()
        .align(Alignment.BottomCenter)
        .background(MaterialTheme.colorScheme.surface),
) {
    Divider(Modifier.fillMaxWidth())

    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(
                vertical = 8.dp,
                horizontal = horizontalPadding,
            ),
        horizontalArrangement = Arrangement.End
    ) {
        Button(
            onClick = { },
            modifier = Modifier.align(Alignment.CenterVertically),
        ) { Text(getString().sign_in_request_authentication) }
    }
}
