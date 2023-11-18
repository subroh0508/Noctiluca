package noctiluca.features.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

val LocalResources = compositionLocalOf { Resources("JA") }
val LocalCommonResources get() = LocalResources

@Composable
fun getCommonString() = getString()

@Composable
fun getCommonDrawables() = getDrawables()
