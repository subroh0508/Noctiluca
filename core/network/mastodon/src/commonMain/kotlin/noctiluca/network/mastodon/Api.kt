package noctiluca.network.mastodon

import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Resource("/api")
@Serializable
object Api {
    @Resource("v1")
    @Serializable
    class V1(val parent: noctiluca.network.mastodon.Api = noctiluca.network.mastodon.Api) {
        @Resource("accounts")
        @Serializable
        class Accounts(val parent: noctiluca.network.mastodon.Api.V1 = noctiluca.network.mastodon.Api.V1()) {
            @Resource("verify_credentials")
            @Serializable
            class VerifyCredentials(val parent: noctiluca.network.mastodon.Api.V1.Accounts = noctiluca.network.mastodon.Api.V1.Accounts())

            @Resource("update_credentials")
            @Serializable
            class UpdateCredentials(val parent: noctiluca.network.mastodon.Api.V1.Accounts = noctiluca.network.mastodon.Api.V1.Accounts())

            @Resource("{id}")
            @Serializable
            class Id(
                val parent: noctiluca.network.mastodon.Api.V1.Accounts = noctiluca.network.mastodon.Api.V1.Accounts(),
                val id: String
            ) {
                @Resource("statuses")
                @Serializable
                class Statuses(val parent: noctiluca.network.mastodon.Api.V1.Accounts.Id)
            }

            @Resource("relationships")
            @Serializable
            class Relationships(val parent: noctiluca.network.mastodon.Api.V1.Accounts = noctiluca.network.mastodon.Api.V1.Accounts())
        }

        @Resource("instance")
        @Serializable
        class Instance(val parent: noctiluca.network.mastodon.Api.V1 = noctiluca.network.mastodon.Api.V1()) {
            @Resource("extended_description")
            @Serializable
            class ExtendedDescription(val parent: noctiluca.network.mastodon.Api.V1.Instance = noctiluca.network.mastodon.Api.V1.Instance())
        }

        @Resource("timelines")
        @Serializable
        class Timelines(val parent: noctiluca.network.mastodon.Api.V1 = noctiluca.network.mastodon.Api.V1()) {
            @Resource("public")
            @Serializable
            class Public(val parent: noctiluca.network.mastodon.Api.V1.Timelines = noctiluca.network.mastodon.Api.V1.Timelines())

            @Resource("home")
            @Serializable
            class Home(val parent: noctiluca.network.mastodon.Api.V1.Timelines = noctiluca.network.mastodon.Api.V1.Timelines())
        }

        @Resource("statuses")
        @Serializable
        class Statuses(val parent: noctiluca.network.mastodon.Api.V1 = noctiluca.network.mastodon.Api.V1()) {
            @Resource("{id}")
            @Serializable
            class Id(
                val parent: noctiluca.network.mastodon.Api.V1.Statuses = noctiluca.network.mastodon.Api.V1.Statuses(),
                val id: String
            ) {
                @Resource("favourite")
                @Serializable
                class Favourite(val parent: noctiluca.network.mastodon.Api.V1.Statuses.Id) {
                    constructor(id: String) : this(
                        noctiluca.network.mastodon.Api.V1.Statuses.Id(id = id)
                    )
                }

                @Resource("unfavourite")
                @Serializable
                class Unfavourite(val parent: noctiluca.network.mastodon.Api.V1.Statuses.Id) {
                    constructor(id: String) : this(
                        noctiluca.network.mastodon.Api.V1.Statuses.Id(id = id)
                    )
                }

                @Resource("reblog")
                @Serializable
                class Reblog(val parent: noctiluca.network.mastodon.Api.V1.Statuses.Id) {
                    constructor(id: String) : this(
                        noctiluca.network.mastodon.Api.V1.Statuses.Id(id = id)
                    )
                }

                @Resource("unreblog")
                @Serializable
                class Unreblog(val parent: noctiluca.network.mastodon.Api.V1.Statuses.Id) {
                    constructor(id: String) : this(
                        noctiluca.network.mastodon.Api.V1.Statuses.Id(id = id)
                    )
                }

                @Resource("bookmark")
                @Serializable
                class Bookmark(val parent: noctiluca.network.mastodon.Api.V1.Statuses.Id) {
                    constructor(id: String) : this(noctiluca.network.mastodon.Api.V1.Statuses.Id(id = id))
                }

                @Resource("unbookmark")
                @Serializable
                class Unbookmark(val parent: noctiluca.network.mastodon.Api.V1.Statuses.Id) {
                    constructor(id: String) : this(noctiluca.network.mastodon.Api.V1.Statuses.Id(id = id))
                }
            }
        }
    }

    @Resource("v2")
    @Serializable
    class V2(val parent: noctiluca.network.mastodon.Api = noctiluca.network.mastodon.Api) {
        @Resource("instance")
        @Serializable
        class Instance(val parent: noctiluca.network.mastodon.Api.V2 = noctiluca.network.mastodon.Api.V2())
    }
}
