package noctiluca.features.accountdetail.component.caption

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.accountdetail.getString
import noctiluca.features.accountdetail.model.RelationshipsStateModel
import noctiluca.model.accountdetail.AccountAttributes
import noctiluca.model.accountdetail.Relationships

@Composable
internal fun RelationshipButton(
    locked: Boolean,
    relationshipsStateModel: RelationshipsStateModel,
    follow: () -> Unit,
    block: () -> Unit,
) = when (relationshipsStateModel.relationships.state(locked)) {
    ButtonType.EDIT_PROFILE -> EditProfileButton {}
    ButtonType.FOLLOW -> FollowButton(follow)
    ButtonType.UNFOLLOW -> UnfollowButton(follow)
    ButtonType.SEND_FOLLOW_REQUEST -> SendFollowRequestButton(follow)
    ButtonType.CANCEL_FOLLOW_REQUEST -> CancelFollowRequestButton(follow)
    ButtonType.UNBLOCK -> UnblockButton(block)
    ButtonType.EMPTY -> Spacer(
        modifier = Modifier.width(ButtonDefaults.MinWidth)
            .padding(vertical = 4.dp)
            .height(ButtonDefaults.MinHeight),
    )
}

private enum class ButtonType {
    EDIT_PROFILE,
    FOLLOW,
    UNFOLLOW,
    SEND_FOLLOW_REQUEST,
    CANCEL_FOLLOW_REQUEST,
    UNBLOCK,
    EMPTY
}

private fun Relationships.state(locked: Boolean) = when {
    me -> ButtonType.EDIT_PROFILE
    blocking -> ButtonType.UNBLOCK
    requested -> ButtonType.CANCEL_FOLLOW_REQUEST
    locked && !following && !blockedBy -> ButtonType.SEND_FOLLOW_REQUEST
    following && !blockedBy -> ButtonType.UNFOLLOW
    !following && !blockedBy -> ButtonType.FOLLOW
    else -> ButtonType.EMPTY
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

@Composable
private fun UnfollowButton(
    onClick: () -> Unit,
) = FilledTonalButton(
    onClick = onClick,
) {
    Text(getString().account_detail_unfollow)
}

@Composable
private fun SendFollowRequestButton(
    onClick: () -> Unit,
) = FilledTonalButton(
    onClick = onClick,
) {
    Icon(
        Icons.Default.Lock,
        contentDescription = "Send Follow Request",
        modifier = Modifier.padding(end = 8.dp)
            .size(18.dp),
    )

    Text(getString().account_detail_send_follow_request)
}

@Composable
private fun CancelFollowRequestButton(
    onClick: () -> Unit,
) = FilledTonalButton(
    onClick = onClick,
) {
    Text(getString().account_detail_cancel_follow_request)
}

@Composable
private fun UnblockButton(
    onClick: () -> Unit,
) = FilledTonalButton(
    onClick = onClick,
) {
    Text(getString().account_detail_unblock)
}
