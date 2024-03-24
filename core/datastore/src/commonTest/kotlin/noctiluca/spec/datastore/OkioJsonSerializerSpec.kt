package noctiluca.spec.datastore

import androidx.datastore.core.DataStoreFactory
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.be
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.should
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import noctiluca.datastore.internal.createOkioStorage
import okio.fakefilesystem.FakeFileSystem

@Serializable
private data class TestData(val id: String, val name: String)

class OkioJsonSerializerSpec : DescribeSpec({
    coroutineTestScope = true

    describe("#save") {
        it("should return Json from data class") {
            val dataStore = DataStoreFactory.create(
                createOkioStorage<TestData?>(
                    Json,
                    defaultValue = null,
                    fileName = "OkioJsonSerializer",
                    fileSystem = FakeFileSystem(),
                )
            )

            dataStore.data.firstOrNull() should beNull()
            dataStore.updateData { TestData("xxx", "テスト") }
            dataStore.data.firstOrNull() should be(TestData("xxx", "テスト"))
        }

        it("should return Json from List") {
            val dataStore = DataStoreFactory.create(
                createOkioStorage(
                    Json,
                    defaultValue = listOf<TestData>(),
                    fileName = "OkioListJsonSerializer",
                    fileSystem = FakeFileSystem(),
                )
            )

            dataStore.data.first() should be(listOf())
            dataStore.updateData { it + TestData("xxx", "テスト") }
            dataStore.data.first() should be(listOf(TestData("xxx", "テスト")))
            dataStore.updateData { it - TestData("xxx", "テスト") }
            dataStore.data.first() should be(listOf())
        }
    }
})
