package noctiluca.test.util

import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.resources.serialization.*

inline fun <reified T> Url.isMatched(
    format: ResourcesFormat,
    resource: T,
) = toString().contains(href(format, resource))
