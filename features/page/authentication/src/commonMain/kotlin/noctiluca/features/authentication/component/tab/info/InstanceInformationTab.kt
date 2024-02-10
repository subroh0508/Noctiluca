package noctiluca.features.authentication.component.tab.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import noctiluca.features.authentication.getString
import noctiluca.features.shared.atoms.image.AsyncImage
import noctiluca.features.shared.atoms.image.NumberCircle
import noctiluca.features.shared.atoms.list.LeadingAvatarContainerSize
import noctiluca.features.shared.atoms.list.Section
import noctiluca.features.shared.atoms.list.SectionItem
import noctiluca.model.authentication.Instance

@Suppress("FunctionNaming")
internal fun LazyListScope.InstanceInformationTab(
    instance: Instance?,
) {
    instance ?: return

    item {
        Column {
            AdministratorSection(instance.administrator)
            StatsSection(instance)
            RulesSection(instance.rules)
            VersionSection(instance.version)
        }
    }
}

@Composable
private fun AdministratorSection(
    administrator: Instance.Administrator,
) = Section(
    getString().sign_in_instance_detail_info_administrator_label,
) {
    SectionItem(
        headlineText = administrator.displayName,
        supportingText = administrator.screen,
        leadingContent = {
            AsyncImage(
                administrator.avatar,
                modifier = Modifier.size(LeadingAvatarContainerSize)
                    .clip(RoundedCornerShape(8.dp)),
            )
        },
        trailingContent = {
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    Icons.Default.Mail,
                    contentDescription = "Mail",
                )
            }
        },
        modifier = Modifier.padding(vertical = 8.dp),
    )
}

@Composable
private fun StatsSection(
    instance: Instance,
) {
    val activeUserCount = instance.activeUserCount ?: return

    Section(
        getString().sign_in_instance_detail_info_instance_stats_label,
    ) {
        SectionItem(
            headlineText = getString().sign_in_instance_detail_info_instance_active_user_count.format(activeUserCount),
            supportingText = getString().sign_in_instance_detail_info_instance_active_user_count_support,
            leadingContent = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "ActiveUserCount",
                    modifier = Modifier.size(LeadingAvatarContainerSize),
                )
            },
            modifier = Modifier.padding(vertical = 8.dp),
        )
    }
}

@Composable
private fun RulesSection(
    rules: List<Instance.Rule>,
) {
    if (rules.isEmpty()) return

    Section(
        getString().sign_in_instance_detail_info_instance_rule_label,
    ) {
        rules.forEachIndexed { i, rule ->
            SectionItem(
                headlineText = rule.text,
                leadingContent = { NumberCircle(i + 1) },
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }
    }
}

@Composable
private fun VersionSection(
    version: Instance.Version?,
) {
    version ?: return

    Section(
        getString().sign_in_instance_detail_info_instance_version_label,
    ) {
        SectionItem(headlineText = "v$version")
    }
}
