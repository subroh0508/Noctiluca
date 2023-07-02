package app.noctiluca.decompose

import androidx.appcompat.app.AppCompatActivity
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.defaultComponentContext

class DefaultRootComponent(
    activity: AppCompatActivity,
) : RootComponent, ComponentContext by activity.defaultComponentContext()
