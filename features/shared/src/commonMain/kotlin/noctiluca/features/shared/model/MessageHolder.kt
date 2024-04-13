package noctiluca.features.shared.model

data class MessageHolder<E : Enum<*>>(
    val text: E? = null,
) {
    fun consume() = copy(text = null)
}
