package noctiluca.api.instancessocial.internal

import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Resource("/api/1.0")
@Serializable
internal object Api {
    @Resource("instances")
    @Serializable
    class Instances(val parent: Api = Api) {
        @Resource("search")
        @Serializable
        class Search(val parent: Instances = Instances())
    }
}
