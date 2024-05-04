package noctiluca.features.signin.mock

import noctiluca.features.signin.HTML_INSTANCE_DESCRIPTION
import noctiluca.features.signin.INSTANCE_DESCRIPTION
import noctiluca.features.signin.INSTANCE_DOMAIN
import noctiluca.features.signin.INSTANCE_NAME
import noctiluca.model.Uri
import noctiluca.model.authorization.Instance

val instance = Instance(
    name = INSTANCE_NAME,
    domain = INSTANCE_DOMAIN,
    description = INSTANCE_DESCRIPTION,
    thumbnail = Uri("https://media.mstdn.social/site_uploads/thumbnail.png"),
    languages = listOf("ja"),
    activeUserCount = 10_000,
    administrator = Instance.Administrator(
        screen = "@stux@mstdn.social",
        displayName = "stux⚡",
        url = Uri("https://mstdn.social/@stux"),
        avatar = Uri("https://media.mstdn.social/accounts/avatars/avatar.png"),
    ),
    rules = listOf(
        Instance.Rule(
            "aaa",
            "Sexually explicit or violent media must be marked as sensitive when posting."
        ),
        Instance.Rule("bbb", "No spam or advertising."),
    ),
    extendedDescription = HTML_INSTANCE_DESCRIPTION,
    version = Instance.Version(4, 2, 8),
)
