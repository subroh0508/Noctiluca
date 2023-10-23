package noctiluca.spec.datastore

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.be
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.should
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import noctiluca.datastore.JsonPreferences
import noctiluca.datastore.add
import noctiluca.datastore.remove
import java.util.prefs.Preferences

@Serializable
private data class TestData(val id: String, val name: String)

class JsonPreferencesSpec : DescribeSpec({
    describe("#save") {
        it("should return Json from data class") {
            val prefs = JsonPreferences<TestData?>(
                Json,
                defaultValue = null,
                Preferences.userNodeForPackage(JsonPreferencesSpec::class.java),
            ).apply { clear() }

            prefs.data should beNull()
            prefs.save(TestData("xxx", "テスト"))
            prefs.data should be(TestData("xxx", "テスト"))
        }

        it("should return Json from List") {
            val prefs = JsonPreferences<List<TestData>>(
                Json,
                defaultValue = listOf(),
                Preferences.userNodeForPackage(JsonPreferencesSpec::class.java),
            ).apply { clear() }

            prefs.data should be(listOf())
            prefs.add(TestData("xxx", "テスト"))
            prefs.data should be(listOf(TestData("xxx", "テスト")))
            prefs.remove(TestData("xxx", "テスト"))
            prefs.data should be(listOf())
        }
    }
})
