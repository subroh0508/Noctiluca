package noctiluca.features.accountdetail.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.accountdetail.getString
import noctiluca.model.accountdetail.Relationships

@Composable
fun RelationshipButton(
    relationships: Relationships,
    onClick: () -> Unit,
) {
    if (relationships == Relationships.ME) {
        EditProfileButton(onClick)
        return
    }

    if (relationships.following) {
        UnfollowButton(onClick)
    } else {
        FollowButton(onClick)
    }
}

@Composable
private fun EditProfileButton(
    onClick: () -> Unit,
) = Button(
    onClick = onClick,
) {
    Icon(
        Icons.Default.Edit,
        contentDescription = "Edit Profile",
        modifier = Modifier.padding(end = 8.dp)
            .size(18.dp),
    )

    Text(getString().account_detail_edit_profile)
}

@Composable
private fun UnfollowButton(
    onClick: () -> Unit,
) = FilledTonalButton(
    onClick = onClick,
) {
    Text(getString().account_detail_following)
}

@Composable
private fun FollowButton(
    onClick: () -> Unit,
) = Button(
    onClick = onClick,
) {
    Icon(
        Icons.Default.Add,
        contentDescription = "Follow",
        modifier = Modifier.padding(end = 8.dp)
            .size(18.dp),
    )

    Text(getString().account_detail_follow)
}
