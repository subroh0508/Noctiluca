package noctiluca.features.shared.utils

actual fun String.format(vararg args: Any?) = String.format(this, *args)
