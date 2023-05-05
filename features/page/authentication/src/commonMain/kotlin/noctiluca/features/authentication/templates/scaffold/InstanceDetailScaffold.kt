package noctiluca.features.authentication.templates.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import noctiluca.features.authentication.getString
import noctiluca.features.authentication.organisms.tab.InstanceDetailScrollState
import noctiluca.features.authentication.organisms.tab.InstanceDetailTabs
import noctiluca.features.authentication.organisms.tab.extendeddescription.InstanceExtendedDescriptionTab
import noctiluca.features.authentication.organisms.tab.info.InstanceInformationTab
import noctiluca.features.authentication.organisms.tab.localtimeline.InstanceLocalTimelineTab
import noctiluca.features.authentication.organisms.tab.rememberTabbedInstanceDetailState
import noctiluca.features.authentication.state.*
import noctiluca.features.authentication.state.rememberLocalTimelineState
import noctiluca.features.authentication.state.rememberMastodonInstanceDetail
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
    domain: String,
    onBackPressed: () -> Unit,
) {
    val instanceLoadState by rememberMastodonInstanceDetail(domain)

    val instance: Instance? = instanceLoadState.getValueOrNull()
    val localTimelineState = rememberLocalTimelineState(domain)
    val tabbedScrollState = rememberTabbedInstanceDetailState(instance)

    HeadlinedScaffold(
        tabbedScrollState.lazyListState,
        tabComposeIndex = 3,
        topAppBar = { scrollBehavior ->
            HeadlineTopAppBar(
                title = {
                    InstanceHeaderText(
                        instance,
                        tabbedScrollState,
                    )
                },
                onBackPressed = onBackPressed,
                scrollBehavior = scrollBehavior,
            )
        },
        loading = { paddingValues -> InstanceLoading(instanceLoadState.loading, paddingValues) },
        bottomBar = { horizontalPadding -> ActionButtons(instance, horizontalPadding) },
        tabs = { InstanceDetailTabs(tabbedScrollState) },
    ) { tabs, horizontalPadding ->
        item { InstanceThumbnail(instance?.thumbnail, horizontalPadding) }
        item { InstanceName(instance, horizontalPadding) }
        item { InstanceDescription(instance, horizontalPadding) }
        item { tabs() }
        InstanceTab(instance, tabbedScrollState, localTimelineState)
    }
}

@Composable
private fun InstanceHeaderText(
    instance: Instance?,
    tabbedScrollState: InstanceDetailScrollState,
) {
    instance ?: return

    HeadlineText(
        instance.name,
        instance.domain,
        tabbedScrollState.lazyListState.firstVisibleItemIndex > 1,
    )
}

@Composable
private fun InstanceLoading(
    loading: Boolean,
    paddingValues: PaddingValues,
) {
    if (!loading) {
        return
    }

    LinearProgressIndicator(
        modifier = Modifier.fillMaxWidth()
            .padding(top = paddingValues.calculateTopPadding()),
    )
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
    instance: Instance?,
    horizontalPadding: Dp,
) {
    instance ?: return

    Column(
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
}

@Composable
private fun InstanceDescription(
    instance: Instance?,
    horizontalPadding: Dp,
) {
    instance ?: return

    HtmlText(
        instance.description ?: "",
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.fillMaxWidth()
            .padding(
                bottom = 16.dp,
                start = horizontalPadding,
                end = horizontalPadding,
            ),
    )
}

@Suppress("FunctionNaming")
private fun LazyListScope.InstanceTab(
    instance: Instance?,
    tabbedScrollState: InstanceDetailScrollState,
    localTimelineState: LocalTimelineState,
) {
    instance ?: return

    when (tabbedScrollState.tab) {
        InstancesTab.INFO -> item { InstanceInformationTab(instance) }
        InstancesTab.EXTENDED_DESCRIPTION -> item { InstanceExtendedDescriptionTab(instance) }
        InstancesTab.LOCAL_TIMELINE -> InstanceLocalTimelineTab(instance, localTimelineState)
    }
}

@Composable
private fun BoxScope.ActionButtons(
    instance: Instance?,
    horizontalPadding: Dp,
) {
    instance ?: return

    val authorizedUserState = rememberAuthorizedUser(instance.domain)
    val scope = rememberCoroutineScope()

    Column(
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
                onClick = { authorizedUserState.requestAuthorize(scope, instance) },
                modifier = Modifier.align(Alignment.CenterVertically),
            ) { Text(getString().sign_in_request_authentication) }
        }
    }
}
