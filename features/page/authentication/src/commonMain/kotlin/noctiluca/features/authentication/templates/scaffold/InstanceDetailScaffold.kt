package noctiluca.features.authentication.templates.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import noctiluca.features.authentication.getString
import noctiluca.features.components.atoms.text.HtmlText
import noctiluca.features.components.molecules.scaffold.HeadlineHeader
import noctiluca.features.components.molecules.scaffold.HeadlineText
import noctiluca.features.components.molecules.scaffold.HeadlineTopAppBar
import noctiluca.features.components.molecules.scaffold.HeadlinedScaffold
import noctiluca.instance.model.Instance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InstanceDetailScaffold(
    instance: Instance,
    onBackPressed: () -> Unit,
) = HeadlinedScaffold(
    topAppBar = { scrollBehavior ->
        HeadlineTopAppBar(
            title = { isScrolled ->
                HeadlineText(
                    instance.name,
                    instance.domain,
                    isScrolled,
                )
            },
            contentScrollOffset = -300F,
            onBackPressed = onBackPressed,
            scrollBehavior = scrollBehavior,
        )
    },
    header = { scrollBehavior ->
        HeadlineHeader(
            instance.thumbnail,
            scrollBehavior,
            contentScale = ContentScale.FillWidth,
        )
    },
) { paddingValues, horizontalPadding ->
    Box(
        modifier = Modifier.fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding()),
    ) {
        InstanceDetailCaption(
            instance,
            modifier = Modifier.padding(horizontal = horizontalPadding),
        )

        ActionButtons(horizontalPadding)
    }
}

@Composable
private fun InstanceDetailCaption(
    instance: Instance,
    modifier: Modifier = Modifier,
) = Column(
    modifier = Modifier.fillMaxWidth()
        .then(modifier),
) {
    Spacer(modifier = Modifier.height(16.dp))

    Text(
        instance.name,
        style = MaterialTheme.typography.headlineSmall,
    )

    Text(
        instance.domain,
        color = MaterialTheme.colorScheme.outline,
        style = MaterialTheme.typography.bodyLarge,
    )

    Spacer(modifier = Modifier.height(16.dp))

    HtmlText(
        instance.description ?: "",
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun BoxScope.ActionButtons(
    horizontalPadding: Dp,
) = Column(
    modifier = Modifier.fillMaxWidth()
        .align(Alignment.BottomCenter),
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
