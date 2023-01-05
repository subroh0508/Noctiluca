package noctiluca.api.mastodon.internal

import io.ktor.resources.*

@Resource("/api")
object Api {
    @Resource("v1")
    class V1(val parent: Api = Api) {
        @Resource("accounts")
        class Accounts(val parent: V1 = V1()) {
            @Resource("verify_credentials")
            class VerifyCredentials(val parent: Accounts = Accounts())
            @Resource("update_credentials")
            class UpdateCredentials(val parent: Accounts = Accounts())
        }

        @Resource("instance")
        class Instance(val parent: V1 = V1())

        @Resource("timelines")
        class Timelines(val parent: V1 = V1()) {
            @Resource("public")
            class Public(val parent: Timelines = Timelines())
        }
    }
}
