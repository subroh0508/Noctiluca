package noctiluca.features.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator

data class StatusDetail(val id: String) : ScreenProvider

fun Navigator.navigateToStatusDetail(
    id: String,
) = push(ScreenRegistry.get(StatusDetail(id)))
