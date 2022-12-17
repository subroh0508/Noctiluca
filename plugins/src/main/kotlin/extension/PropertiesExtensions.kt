package extension

import org.gradle.api.Project
import java.util.*

fun Project.localProperties(): Properties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}
