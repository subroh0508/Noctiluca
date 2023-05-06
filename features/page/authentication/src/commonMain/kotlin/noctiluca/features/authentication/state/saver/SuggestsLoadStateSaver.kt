package noctiluca.features.authentication.state.saver

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import noctiluca.features.components.model.LoadState
import noctiluca.instance.model.Instance
import noctiluca.model.Uri

private const val KEY_QUERY = "query"
private const val KEY_DATA = "data"

private const val PROPERTY_DOMAIN = "domain"
private const val PROPERTY_DESCRIPTION = "description"
private const val PROPERTY_THUMBNAIL = "thumbnail"
private const val PROPERTY_VERSION = "version"

val SuggestsLoadStateSaver: Saver<Pair<String, LoadState>, Any> = mapSaver(
    save = { (query, loadState) ->
        val data: List<Instance.Suggest> = loadState.getValueOrNull() ?: return@mapSaver mapOf()

        mapOf(
            KEY_QUERY to query,
            KEY_DATA to data.map { datum ->
                mapOf(
                    PROPERTY_DOMAIN to datum.domain,
                    PROPERTY_DESCRIPTION to datum.description,
                    PROPERTY_THUMBNAIL to datum.thumbnail?.value,
                    PROPERTY_VERSION to datum.version?.toString(),
                )
            }.toTypedArray(),
        )
    },
    restore = { map ->
        val query = map[KEY_QUERY] as? String ?: ""
        val data = map[KEY_DATA] as? Array<*> ?: return@mapSaver query to LoadState.Initial

        val suggests = data.mapNotNull {
            val datum = it as? Map<*, *> ?: return@mapNotNull null
            val domain = datum[PROPERTY_DOMAIN] as? String ?: return@mapNotNull null
            val description = datum[PROPERTY_DESCRIPTION] as? String
            val thumbnail = datum[PROPERTY_THUMBNAIL] as? String
            val version = datum[PROPERTY_VERSION] as? String

            Instance.Suggest(
                domain = domain,
                description = description,
                thumbnail = thumbnail?.let(::Uri),
                version = version?.let { v -> Instance.Version(v) },
            )
        }

        query to LoadState.Loaded(suggests)
    },
)
