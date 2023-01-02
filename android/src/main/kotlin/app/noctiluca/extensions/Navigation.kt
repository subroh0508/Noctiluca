package app.noctiluca.extensions

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController

val NavBackStackEntry.uri: Uri?
    get() = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
                (arguments?.getParcelable(NavController.KEY_DEEP_LINK_INTENT) as? Intent)?.data
            else
                arguments?.getParcelable(NavController.KEY_DEEP_LINK_INTENT, Intent::class.java)?.data
