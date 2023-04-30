package noctiluca.features.components.utils

import androidx.compose.runtime.Composable
import noctiluca.features.components.getCommonString

@Composable
fun format(
    n: Int,
    pattern: String = getCommonString().amount,
) = pattern.format(n)
