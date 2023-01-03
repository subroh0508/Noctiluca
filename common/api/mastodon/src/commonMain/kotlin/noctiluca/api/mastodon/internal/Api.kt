package noctiluca.api.mastodon.internal

import noctiluca.api.mastodon.internal.builder.EndpointPath

internal object Api : EndpointPath() {
    object V1 : EndpointPath(path) {
        object Accounts : EndpointPath(path) {
            val VerifyCredentials by this
            val UpdateCredentials by this
        }

        val Instance by this
    }
}
