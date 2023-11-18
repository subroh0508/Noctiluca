package noctiluca.features.shared.utils

import androidx.compose.runtime.Composable
import noctiluca.features.shared.getCommonString

@Composable
fun format(
    n: Int,
    pattern: String = getCommonString().amount,
) = pattern.format(n)
