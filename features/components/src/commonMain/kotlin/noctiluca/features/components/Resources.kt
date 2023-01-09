package noctiluca.features.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import noctiluca.features.components.Resources

val LocalResources = compositionLocalOf { Resources("JA") }
val LocalCommonResources get() = LocalResources

@Composable
fun getCommonString() = getString()

@Composable
fun getCommonDrawables() = getDrawables()
