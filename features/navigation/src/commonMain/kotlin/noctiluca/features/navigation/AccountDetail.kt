package noctiluca.features.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator
import noctiluca.model.AccountId

data class AccountDetailScreen(val id: String) : ScreenProvider

fun Navigator.navigateToAccountDetail(
    id: AccountId,
) = push(ScreenRegistry.get(AccountDetailScreen(id.value)))
