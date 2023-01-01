package tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import tasks.resources.DrawableResources
import tasks.resources.ResourcesWrapper
import tasks.resources.StringResources
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

// @see: https://github.com/netguru/compose-multiplatform-charts/blob/main/buildSrc/src/main/java/com/netguru/multiplatform/charts/resources/ResourceGeneratorTask.kt
abstract class ResourceGeneratorTask : DefaultTask() {
    private val packageName get() = "noctiluca.features.${project.name}"

    private val stringResources = StringResources(packageName)
    private val drawableResource = DrawableResources(packageName)
    private val resourcesWrapperClass = ResourcesWrapper(packageName)
    private val file = File(project.buildDir, "generated/resources")

    @TaskAction
    fun generateResources() {
        project.plugins.withType(KotlinMultiplatformPluginWrapper::class) {
            val multiplatformExt = project.extensions.getByType(KotlinMultiplatformExtension::class)
            val commonSourceSet = multiplatformExt.sourceSets.getByName("commonMain")
            val resources = commonSourceSet.resources.matching { include("/**/*.*") }

            readResources(resources.files)

            resourcesWrapperClass.create(stringResources, drawableResource).writeTo(file)
        }
    }

    private fun readResources(files: Set<File>) = files
        .forEach {
            try {
                val fileName = it.name.substringAfterLast("/").substringBeforeLast(".")
                val directoryName = it.toString().substringBeforeLast("/").substringAfterLast("/")

                when {
                    fileName == "strings" -> {
                        val language = directoryName.replace("values", "")
                            .substringAfterLast("-")
                            .takeIf(String::isNotBlank) ?: StringResources.BASE_LANGUAGE_CODE

                        stringResources.setCommonStrings(readXml("string", it, translatable = false))
                        stringResources.setStrings(language, readXml("string", it))
                    }
                    directoryName == "drawable" -> {
                        drawableResource.setDrawable(readFileName(it))
                    }
                }
            } catch (e: Exception) {
                println("error during parsing $e")
            }
        }

    private fun readXml(
        type: String,
        file: File,
        translatable: Boolean? = null,
    ): MutableMap<String, String> {
        val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
        val elements = document.getElementsByTagName(type)
        val parsedElements = mutableMapOf<String, String>()
        for (elementIndex in 0 until elements.length) {
            val lineContent = elements.item(elementIndex)

            if (lineContent.attributes.getNamedItem("translatable")?.textContent == translatable.toString()) {
                continue
            }
            val name = lineContent.attributes.getNamedItem("name").textContent
            val value = lineContent.textContent.replace("\\", "")
            parsedElements[name] = value
        }
        return parsedElements
    }

    private fun readFileName(file: File): Pair<String, String> {
        val fileExtension = file.name.substringAfterLast("/")
        val fileName = fileExtension.substringBeforeLast(".")
        return fileName to fileExtension
    }
}
