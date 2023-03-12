package noctiluca.spec.datastore

import android.content.Context
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.kotest.common.runBlocking
import io.kotest.matchers.be
import io.kotest.matchers.collections.haveSize
import io.kotest.matchers.should
import kotlin.test.Test
import kotlinx.coroutines.flow.toList
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import noctiluca.datastore.JsonSerializer
import noctiluca.datastore.getJsonDataStore
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class JsonSerializerSpec {
    @Serializable
    private class TestData(val id: String, val name: String)

    private val context: Context get() = getApplicationContext()

    private inline fun <reified T : Any?> buildDataStore(
        defaultValue: T
    ) = context.getJsonDataStore(
        JsonSerializer(Json, defaultValue),
        JsonSerializerSpec::class.simpleName ?: "",
    )

    @Test
    fun serialize_shouldReturnJsonFromDataClass() {
        val dataStore = buildDataStore<TestData?>(defaultValue = null)

        runBlocking {
            dataStore.updateData { TestData("xxx", "テスト") }
            dataStore.data.toList().let {
                it should haveSize(1)
                it.first() should be(TestData("xxx", "テスト"))
            }
        }
    }
}
