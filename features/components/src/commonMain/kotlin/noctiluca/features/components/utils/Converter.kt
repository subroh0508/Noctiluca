package noctiluca.features.components.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit

@Composable
fun TextUnit.toDp() = with(LocalDensity.current) { toDp() }

@Composable
fun Int.toDp() = with(LocalDensity.current) { toDp() }

@Composable
fun Float.toDp() = with(LocalDensity.current) { toDp() }
