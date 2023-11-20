package noctiluca.features.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import com.seiko.imageloader.LocalImageLoader
import org.koin.mp.KoinPlatform.getKoin

@Composable
fun FeatureComposable(
    vararg values: ProvidedValue<*>,
    content: @Composable () -> Unit,
) = CompositionLocalProvider(
    LocalImageLoader provides getKoin().get(),
    LocalCommonResources provides Resources("JA"),
    *values,
) { content() }
