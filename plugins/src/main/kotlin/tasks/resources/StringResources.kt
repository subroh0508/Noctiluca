package tasks.resources

import com.squareup.kotlinpoet.*

class StringResources(packageName: String) : Resources {
    companion object {
        const val BASE_LANGUAGE_CODE = "ja"
    }

    private val composableAnnotation = ClassName("androidx.compose.runtime", "Composable")
    private val readOnlyComposableAnnotation = ClassName("androidx.compose.runtime", "ReadOnlyComposable")
    private val stringResourceClass = ClassName(packageName, "StringResources")
    private val stringSets = mutableMapOf<String, Map<String, String>>()
    private val commonStringSets = mutableMapOf<String, String>()

    fun setStrings(language: String, strings: Map<String, String>) {
        stringSets[language] = strings
    }

    fun setCommonStrings(strings: Map<String, String>) {
        commonStringSets += strings
    }

    override fun addToWrapper(wrapper: TypeSpec.Builder) = with(wrapper) {
        addTypes(createLanguageClass())
        addFunction(createGetStringMethod())
    }

    override fun addToFile(file: FileSpec.Builder) = with(file) {
        addType(createStringResourceClass())
        addFunction(createGetStringHelper())
    }

    private fun createLanguageClass() = stringSets.map { (key, strings) ->
        createLanguageClass(key, strings + commonStringSets)
    }

    private fun createLanguageClass(
        key: String,
        language: Map<String, String>,
    ): TypeSpec {
        val languageCode = key.toUpperCase()
        val languagesProperties: List<PropertySpec> =
            if (key == BASE_LANGUAGE_CODE) {
                emptyList()
            } else {
                language.map { it.createLanguageProperty() }
            }

        return TypeSpec.objectBuilder(languageCode)
            .superclass(stringResourceClass)
            .addAnnotation(immutableAnnotation)
            .addProperties(languagesProperties)
            .addModifiers(KModifier.PRIVATE)
            .build()
    }

    private fun Map.Entry<String, Any>.createLanguageProperty() =
        PropertySpec.builder(
            key.toLowerCase(),
            String::class,
            KModifier.OVERRIDE,
        ).initializer("%S", value)
            .build()

    private fun createGetStringMethod() = FunSpec.builder("getString")
        .returns(stringResourceClass)
        .apply {
            addStatement("val language = when(locale.uppercase()) {")
            stringSets
                .filterNot { it.key.contains(BASE_LANGUAGE_CODE) }
                .forEach {
                    val lang = it.key.substringAfterLast("_").toUpperCase()
                    addStatement("\"${lang}\" -> $lang")
                }
            addStatement("else -> ${BASE_LANGUAGE_CODE.toUpperCase()}")
            addStatement("}")
            addStatement("return language")
        }.build()

    private fun createStringResourceClass() =
        TypeSpec
            .classBuilder(stringResourceClass)
            .addAnnotation(immutableAnnotation)
            .addModifiers(KModifier.ABSTRACT)
            .apply {
                stringSets[BASE_LANGUAGE_CODE]
                    ?.toMap()
                    ?.forEach { addPropertyToStringResource(it) }
            }.build()

    private fun createGetStringHelper() = FunSpec.builder("getString")
        .returns(stringResourceClass)
        .addAnnotation(readOnlyComposableAnnotation)
        .addAnnotation(composableAnnotation)
        .apply {
            addStatement("return LocalResources.current.getString()")
        }.build()

    private fun TypeSpec.Builder.addPropertyToStringResource(language: Map.Entry<String, Any>) {
        val prop = PropertySpec.builder(
            language.key.toLowerCase(),
            String::class,
            KModifier.OPEN,
        ).initializer("%S", language.value).build()
        addProperty(prop)
    }
}
