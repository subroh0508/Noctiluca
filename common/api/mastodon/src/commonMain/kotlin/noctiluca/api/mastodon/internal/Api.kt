package noctiluca.api.mastodon.internal

import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Resource("/api")
@Serializable
internal object Api {
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

        @Resource("streaming")
        @Serializable
        class Streaming(val parent: V1 = V1())
    }
}
