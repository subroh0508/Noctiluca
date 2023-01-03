package noctiluca.api.mastodon.internal.builder

import kotlin.reflect.KProperty

internal open class EndpointPath(
    protected val path: MutableList<String> = mutableListOf(),

) {
    init {
        val name = this::class.simpleName?.toSnakeCase() ?: ""

        if (name.isNotBlank()) {
            path.add(name)
        }
    }

    operator fun getValue(
        thisRef: EndpointPath,
        property: KProperty<*>
    ) = (path + property.name.toSnakeCase()).joinToString("/", prefix = "/")
}

private fun String.toSnakeCase(): String {
    if (matches("""^[a-zA-Z0-9]+$""".toRegex())) {
        return foldIndexed("") { i, acc, c ->
            if (i == 0 && c.isUpperCase()) {
                return@foldIndexed acc + c.lowercase()
            }

            if (c.isUpperCase()) {
                return@foldIndexed acc  + "_" + c.lowercase()
            }

            acc + c
        }
    }

    throw IllegalStateException()
}
