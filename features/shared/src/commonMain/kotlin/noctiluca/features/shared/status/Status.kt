package noctiluca.features.shared.status

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDateTime
import noctiluca.features.components.atoms.image.AsyncImage
import noctiluca.features.components.atoms.text.HtmlText
import noctiluca.features.components.atoms.text.RelativeTime
import noctiluca.features.shared.account.TooterName
import noctiluca.status.model.Status
import noctiluca.status.model.Tooter

private val TooterIconSize = 40.dp

@Composable
fun Status(
    status: Status,
    modifier: Modifier = Modifier,
) = Row(
    modifier = Modifier.padding(
        top = 12.dp,
        bottom = 12.dp,
        start = 16.dp,
        end = 24.dp,
    ).then(modifier),
) {
    AsyncImage(
        status.tooter.avatar,
        //fallback = imageResources(getDrawables().icon_mastodon),
        modifier = Modifier.size(TooterIconSize),
    )

    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
    ) {
        StatusHeader(
            status.tooter,
            status.createdAt,
        )

        HtmlText(
            status.content,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun StatusHeader(
    tooter: Tooter,
    createdAt: LocalDateTime,
) = Row(Modifier.fillMaxWidth()) {
    TooterName(
        tooter,
        modifier = Modifier.weight(1F, true),
    )

    RelativeTime(
        createdAt,
        modifier = Modifier.padding(start = 16.dp),
        color = MaterialTheme.colorScheme.outline,
        style = MaterialTheme.typography.titleSmall,
    )
}
