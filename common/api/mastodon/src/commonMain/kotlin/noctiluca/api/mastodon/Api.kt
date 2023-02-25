package noctiluca.api.mastodon

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
        }

        @Resource("instance")
        @Serializable
        class Instance(val parent: V1 = V1())

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
                @Resource("favourite")
                @Serializable
                class Favourite(val parent: Id) { constructor(id: String) : this(Id(id = id)) }

                @Resource("unfavourite")
                @Serializable
                class Unfavourite(val parent: Id) { constructor(id: String) : this(Id(id = id)) }

                @Resource("reblog")
                @Serializable
                class Reblog(val parent: Id) { constructor(id: String) : this(Id(id = id)) }

                @Resource("unreblog")
                @Serializable
                class Unreblog(val parent: Id) { constructor(id: String) : this(Id(id = id)) }

                @Resource("bookmark")
                @Serializable
                class Bookmark(val parent: Id) { constructor(id: String) : this(Id(id = id)) }

                @Resource("unbookmark")
                @Serializable
                class Unbookmark(val parent: Id) { constructor(id: String) : this(Id(id = id)) }
            }
        }
    }
}
