package noctiluca.features.authentication.organisims

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import noctiluca.features.authentication.organisms.InstanceList
import noctiluca.instance.model.Instance
import noctiluca.theme.NoctilucaTheme

@Preview
@Composable
fun InstanceList_Preview() {

    NoctilucaTheme {
        InstanceList(
            listOf(
                Instance(
                    "mastodon.net",
                    "マストドンサーバーですよ",
                    null,
                    listOf("ja"),
                    100,
                    10_000,
                    Instance.Version("4.0.2"),
                )
            )
        )
    }
}
