package noctiluca.features.authentication.organisms

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import noctiluca.instance.model.Instance
import noctiluca.theme.NoctilucaTheme

private val instances = listOf(
    Instance.Suggest(
        "mastodon.net",
        "マストドンサーバーですよ\nほげほげ",
        null,
        Instance.Version("4.0.2"),
    ),
)

@Preview
@Composable
internal fun InstanceListPreview() {
    NoctilucaTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            /*
            SearchInstanceListLayout(
                LoadState.Loaded(instances),
                instances,
                "mastodon.net",
                onChangeQuery = {},
                onSelect = {},
                modifier = Modifier.padding(16.dp),
            )
            */
        }
    }
}
