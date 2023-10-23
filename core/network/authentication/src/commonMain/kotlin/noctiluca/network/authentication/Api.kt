package noctiluca.network.authentication

import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Resource("/api")
@Serializable
object Api {
    @Resource("v1")
    @Serializable
    class V1(val parent: Api = Api) {
        @Resource("apps")
        @Serializable
        class Apps(val parent: V1 = V1())

        @Resource("accounts")
        @Serializable
        class Accounts(val parent: V1 = V1()) {
            @Resource("verify_credentials")
            @Serializable
            class VerifyCredentials(val parent: Accounts = Accounts())
        }
    }
}

@Resource("/oauth")
@Serializable
object OAuth {
    @Resource("authorize")
    @Serializable
    class Authorize(val parent: OAuth = OAuth)

    @Resource("token")
    @Serializable
    class Token(val parent: OAuth = OAuth)
}
