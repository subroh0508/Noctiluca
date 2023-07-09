package noctiluca.features.components

import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop

interface Navigator {
    companion object {
        operator fun <C : Any> invoke(
            navigation: StackNavigation<C>,
        ): Navigator = Impl(navigation)
    }

    fun backPressed()

    private class Impl<C : Any>(
        private val navigation: StackNavigation<C>,
    ) : Navigator {
        override fun backPressed() = navigation.pop()
    }
}
