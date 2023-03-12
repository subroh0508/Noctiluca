package noctiluca.preferences

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.util.prefs.Preferences

class JsonPreferences<T : Any?>(
    private val json: Json,
    private val serializer: KSerializer<T>,
    private val defaultValue: T,
    private val prefs: Preferences,
) {
    companion object {
        private const val KEY = "data"

        inline operator fun <reified T : Any?> invoke(
            json: Json,
            defaultValue: T,
            prefs: Preferences,
        ) = JsonPreferences(json, serializer(), defaultValue, prefs)
    }

    val data
        get() = prefs.get(KEY, null)?.let {
            json.decodeFromString(serializer, it)
        } ?: defaultValue

    fun save(value: T) {
        prefs.put(KEY, json.encodeToString(serializer, value))
    }

    fun clear() = prefs.clear()
}
