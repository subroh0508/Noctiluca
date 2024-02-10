package noctiluca.features.accountdetail.component.caption

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.NotificationAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.features.accountdetail.getString
import noctiluca.features.accountdetail.model.RelationshipsModel
import noctiluca.model.accountdetail.Relationships

@Composable
internal fun RelationshipButton(
    locked: Boolean,
    model: RelationshipsModel,
    follow: () -> Unit,
    block: () -> Unit,
    notifyNewStatus: () -> Unit,
) = Row {
    if (model.relationships.showNotifyingIcon) {
        NotifyNewStatusIcon(
            onClick = notifyNewStatus,
            model.relationships,
            !model.state.loading,
        )
    }

    when (getButtonType(locked, model)) {
        ButtonType.EDIT_PROFILE -> EditProfileButton {}
        ButtonType.FOLLOW -> FollowButton(follow, !model.state.loading)
        ButtonType.UNFOLLOW -> UnfollowButton(follow, !model.state.loading)
        ButtonType.SEND_FOLLOW_REQUEST -> SendFollowRequestButton(follow, !model.state.loading)
        ButtonType.CANCEL_FOLLOW_REQUEST -> CancelFollowRequestButton(follow, !model.state.loading)
        ButtonType.UNBLOCK -> UnblockButton(block, !model.state.loading)
        ButtonType.EMPTY -> Spacer(
            modifier = Modifier.width(ButtonDefaults.MinWidth)
                .padding(vertical = 4.dp)
                .height(ButtonDefaults.MinHeight),
        )
    }
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

private fun getButtonType(
    locked: Boolean,
    state: RelationshipsModel
) = with(state.relationships) {
    when {
        me -> ButtonType.EDIT_PROFILE
        blocking -> ButtonType.UNBLOCK
        requested -> ButtonType.CANCEL_FOLLOW_REQUEST
        locked && !following && !blockedBy -> ButtonType.SEND_FOLLOW_REQUEST
        following && !blockedBy -> ButtonType.UNFOLLOW
        !following && !blockedBy -> ButtonType.FOLLOW
        else -> ButtonType.EMPTY
    }
}

@Composable
private fun NotifyNewStatusIcon(
    onClick: () -> Unit,
    relationships: Relationships,
    enabled: Boolean,
) {
    val icon =
        if (relationships.notifying) {
            Icons.Default.Notifications
        } else {
            Icons.Outlined.NotificationAdd
        }

    FilledTonalIconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.padding(end = 8.dp),
    ) {
        Icon(icon, contentDescription = "Notify New Status")
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
private fun FollowButton(
    onClick: () -> Unit,
    enabled: Boolean,
) = Button(
    onClick = onClick,
    enabled = enabled,
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
    enabled: Boolean,
) = FilledTonalButton(
    onClick = onClick,
    enabled = enabled,
) {
    Text(getString().account_detail_unfollow)
}

@Composable
private fun SendFollowRequestButton(
    onClick: () -> Unit,
    enabled: Boolean,
) = FilledTonalButton(
    onClick = onClick,
    enabled = enabled,
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
    enabled: Boolean,
) = FilledTonalButton(
    onClick = onClick,
    enabled = enabled,
) {
    Text(getString().account_detail_cancel_follow_request)
}

@Composable
private fun UnblockButton(
    onClick: () -> Unit,
    enabled: Boolean,
) = FilledTonalButton(
    onClick = onClick,
    enabled = enabled,
) {
    Text(getString().account_detail_unblock)
}

private val Relationships.showNotifyingIcon get() = !blocking && !blockedBy && following
