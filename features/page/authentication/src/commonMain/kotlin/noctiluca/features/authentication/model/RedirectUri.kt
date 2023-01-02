package noctiluca.features.authentication.model

import androidx.compose.runtime.Composable
import noctiluca.features.authentication.getString
import noctiluca.model.Uri

@Composable
fun buildRedirectUri() = Uri("${getString().sign_in_oauth_scheme}://${getString().sign_in_client_name}")
