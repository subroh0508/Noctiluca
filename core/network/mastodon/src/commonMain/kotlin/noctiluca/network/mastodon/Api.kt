package noctiluca.network.mastodon

import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Resource("/api")
@Serializable
object Api {
    @Resource("v1")
    @Serializable
    class V1(val parent: Api = Api) {
        @Resource("accounts")
        @Serializable
        class Accounts(val parent: V1 = V1()) {
            @Resource("verify_credentials")
            @Serializable
            class VerifyCredentials(val parent: Accounts = Accounts())

            @Resource("update_credentials")
            @Serializable
            class UpdateCredentials(val parent: Accounts = Accounts())

            @Resource("{id}")
            @Serializable
            class Id(val parent: Accounts = Accounts(), val id: String) {
                @Resource("statuses")
                @Serializable
                class Statuses(val parent: Id)

                @Resource("follow")
                @Serializable
                class Follow(val parent: Id)

                @Resource("unfollow")
                @Serializable
                class Unfollow(val parent: Id)

                @Resource("block")
                @Serializable
                class Block(val parent: Id)

                @Resource("unblock")
                @Serializable
                class Unblock(val parent: Id)

                @Resource("mute")
                @Serializable
                class Mute(val parent: Id)

                @Resource("unmute")
                @Serializable
                class Unmute(val parent: Id)
            }

            @Resource("relationships")
            @Serializable
            class Relationships(val parent: Accounts = Accounts())
        }

        @Resource("instance")
        @Serializable
        class Instance(val parent: V1 = V1()) {
            @Resource("extended_description")
            @Serializable
            class ExtendedDescription(val parent: Instance = Instance())
        }

        @Resource("timelines")
        @Serializable
        class Timelines(val parent: V1 = V1()) {
            @Resource("public")
            @Serializable
            class Public(val parent: Timelines = Timelines())

            @Resource("home")
            @Serializable
            class Home(val parent: Timelines = Timelines())
        }

        @Resource("statuses")
        @Serializable
        class Statuses(val parent: V1 = V1()) {
            @Resource("{id}")
            @Serializable
            class Id(val parent: Statuses = Statuses(), val id: String) {
                @Resource("context")
                @Serializable
                class Context(val parent: Id) {
                    constructor(id: String) : this(Id(id = id))
                }

                @Resource("favourite")
                @Serializable
                class Favourite(val parent: Id) {
                    constructor(id: String) : this(Id(id = id))
                }

                @Resource("unfavourite")
                @Serializable
                class Unfavourite(val parent: Id) {
                    constructor(id: String) : this(Id(id = id))
                }

                @Resource("reblog")
                @Serializable
                class Reblog(val parent: Id) {
                    constructor(id: String) : this(Id(id = id))
                }

                @Resource("unreblog")
                @Serializable
                class Unreblog(val parent: Id) {
                    constructor(id: String) : this(Id(id = id))
                }

                @Resource("bookmark")
                @Serializable
                class Bookmark(val parent: Id) {
                    constructor(id: String) : this(Id(id = id))
                }

                @Resource("unbookmark")
                @Serializable
                class Unbookmark(val parent: Id) {
                    constructor(id: String) : this(Id(id = id))
                }
            }
        }
    }

    @Resource("v2")
    @Serializable
    class V2(val parent: Api = Api) {
        @Resource("instance")
        @Serializable
        class Instance(val parent: V2 = V2())
    }
}
