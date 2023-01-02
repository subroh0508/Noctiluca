package noctiluca.features.authentication.model

import noctiluca.instance.model.Instance

internal sealed class QueryText {
    abstract val text: String

    object Empty : QueryText() { override val text = "" }
    data class Editable(override val text: String) : QueryText()
    data class Static(override val text: String) : QueryText()

    companion object {
        operator fun invoke(text: String): QueryText = if (text.isBlank()) Empty else Editable(text)
        operator fun invoke(suggest: Instance.Suggest): QueryText = Static(suggest.domain)
    }
}
