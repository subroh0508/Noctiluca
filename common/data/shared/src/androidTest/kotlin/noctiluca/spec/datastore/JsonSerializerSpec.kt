package noctiluca.spec.datastore

import android.content.Context
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.kotest.matchers.be
import io.kotest.matchers.collections.beEmpty
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.should
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlin.test.Test
import kotlinx.coroutines.test.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import noctiluca.datastore.JsonSerializer
import noctiluca.datastore.getJsonDataStore
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class JsonSerializerSpec {
    @Serializable
    private data class TestData(val id: String, val name: String)

    private val context: Context get() = getApplicationContext()

    private inline fun <reified T : Any?> buildDataStore(
        defaultValue: T,
    ) = context.getJsonDataStore(
        JsonSerializer(Json, defaultValue),
        JsonSerializerSpec::class.simpleName ?: "",
    )

    @Test
    fun shouldReturnJsonFromDataClass() = runTest {
        val dataStore = buildDataStore<TestData?>(defaultValue = null)

        dataStore.data.firstOrNull() should beNull()
        dataStore.updateData { TestData("xxx", "テスト") }
        dataStore.data.firstOrNull() should be(TestData("xxx", "テスト"))
    }

    @Test
    fun shouldReturnJsonFromList() = runTest {
        val dataStore = buildDataStore<List<TestData>>(defaultValue = listOf())

        dataStore.data.first() should beEmpty()
        dataStore.updateData { listOf(TestData("xxx", "テスト")) }
        dataStore.data.first() should be(listOf(TestData("xxx", "テスト")))
    }
}
