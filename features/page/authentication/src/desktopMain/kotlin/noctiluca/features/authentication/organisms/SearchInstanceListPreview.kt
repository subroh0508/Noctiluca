package noctiluca.features.authentication.organisms

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noctiluca.instance.model.Instance
import noctiluca.theme.NoctilucaTheme

private val instances = listOf(
    Instance(
        "mastodon.net",
        "マストドンサーバーですよ\nほげほげ",
        null,
        listOf("ja"),
        100,
        10_000,
        Instance.Version("4.0.2"),
    ),
)

@Preview
@Composable
internal fun InstanceListPreview() {
    NoctilucaTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(16.dp)) {
                InstanceDomain("mastodon.net", onChangeQuery = {})
                InstanceList(instances)
            }
        }
    }
}
