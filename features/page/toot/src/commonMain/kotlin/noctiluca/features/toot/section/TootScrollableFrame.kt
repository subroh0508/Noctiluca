package noctiluca.features.toot.section

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import noctiluca.features.toot.model.Message

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TootScrollableFrame(
    message: Message?,
    topBar: @Composable (TopAppBarScrollBehavior) -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = { topBar(scrollBehavior) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(top = 8.dp),
        ) { content(paddingValues) }

        if (message == Message.SENDING) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
                    .offset(y = paddingValues.calculateTopPadding()),
            )
        }
    }
}
