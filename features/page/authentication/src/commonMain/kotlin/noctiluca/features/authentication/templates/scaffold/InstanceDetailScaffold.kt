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
import androidx.compose.ui.unit.LayoutDirection
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
import noctiluca.features.components.atoms.card.CardHeader
import noctiluca.features.components.atoms.card.CardSupporting
import noctiluca.features.components.atoms.card.FilledCard
import noctiluca.features.components.atoms.image.AsyncImage
import noctiluca.features.components.atoms.snackbar.showSnackbar
import noctiluca.features.components.atoms.text.HtmlText
import noctiluca.features.components.getCommonString
import noctiluca.features.components.molecules.scaffold.*
import noctiluca.features.components.utils.description
import noctiluca.features.components.utils.label
import noctiluca.instance.model.Instance
import noctiluca.model.Uri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InstanceDetailScaffold(
    domain: String,
    onBackPressed: () -> Unit,
) {
    val instanceLoadState by rememberMastodonInstanceDetail(domain)

    val localTimelineState = rememberLocalTimelineState(domain)
    val tabbedScrollState = rememberTabbedInstanceDetailState(instanceLoadState.getValueOrNull())

    LoadStateHeadlinedScaffold<Instance>(
        instanceLoadState,
        tabbedScrollState.lazyListState,
        tabComposeIndex = 3,
        topAppBar = { scrollBehavior, job, instance ->
            HeadlineTopAppBar(
                title = {
                    InstanceHeaderText(
                        domain,
                        instance,
                        tabbedScrollState,
                    )
                },
                onBackPressed = {
                    job?.cancel()
                    onBackPressed()
                },
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = { instance, horizontalPadding ->
            ActionButtons(
                instance,
                horizontalPadding,
            )
        },
        tabs = { InstanceDetailTabs(tabbedScrollState) },
        fallback = { error, paddingValues ->
            Fallback(
                error,
                paddingValues,
                onBackPressed,
            )
        },
    ) { instance, tabs, horizontalPadding ->
        item { InstanceThumbnail(instance.thumbnail, horizontalPadding) }
        item { InstanceName(instance, horizontalPadding) }
        item { InstanceDescription(instance, horizontalPadding) }
        item { tabs() }
        InstanceTab(instance, tabbedScrollState, localTimelineState)
    }
}

@Composable
private fun InstanceHeaderText(
    domain: String,
    instance: Instance?,
    tabbedScrollState: InstanceDetailScrollState,
) {
    if (instance != null) {
        HeadlineText(
            instance.name,
            instance.domain,
            tabbedScrollState.lazyListState.firstVisibleItemIndex > 1,
        )
        return
    }

    Text(
        domain,
        style = MaterialTheme.typography.titleLarge,
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

@Suppress("FunctionNaming")
private fun LazyListScope.InstanceTab(
    instance: Instance,
    tabbedScrollState: InstanceDetailScrollState,
    localTimelineState: LocalTimelineState,
) = when (tabbedScrollState.tab) {
    InstancesTab.INFO -> item { InstanceInformationTab(instance) }
    InstancesTab.EXTENDED_DESCRIPTION -> item { InstanceExtendedDescriptionTab(instance) }
    InstancesTab.LOCAL_TIMELINE -> InstanceLocalTimelineTab(instance, localTimelineState)
}

@Composable
private fun BoxScope.ActionButtons(
    instance: Instance,
    horizontalPadding: Dp,
) {
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

@Composable
private fun Fallback(
    error: Throwable?,
    paddingValues: PaddingValues,
    onBackPressed: () -> Unit,
) {
    error ?: return

    FilledCard(
        headline = { CardHeader(error.label()) },
        supporting = { CardSupporting(error.description()) },
        actions = {
            Button(onClick = onBackPressed) {
                Text(getCommonString().back)
            }
        },
        modifier = Modifier.padding(
            top = paddingValues.calculateTopPadding(),
            start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
            end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
        ),
    )
}

@Composable
private fun Snackbar(
    error: Throwable,
) = showSnackbar(
    buildString {
        append(getString().sign_in_request_authentication_error)
        append(error.message ?: "")
    },
)
