package noctiluca.features.shared.model

data class MessageHolder(
    val text: String,
    val consumed: Boolean = false,
) {
    fun consume() = copy(consumed = true)
}
