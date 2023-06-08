package noctiluca.features.components

import noctiluca.model.Uri

interface Navigation {
    fun backPressed()
    fun openBrowser(uri: Uri)
    fun reopenApp()
    fun backToSignIn()
}
