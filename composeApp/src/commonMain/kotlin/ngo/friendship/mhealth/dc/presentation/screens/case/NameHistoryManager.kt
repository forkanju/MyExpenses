package ngo.friendship.mhealth.dc.presentation.screens.case

import androidx.compose.ui.text.input.TextFieldValue
import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json

class NameHistoryManager(
    private val settings: Settings,
    private val key: String = "name_history",
    private val maxItems: Int = 10
) {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    fun getHistory(): List<String> {
        val raw = settings.getStringOrNull(key) ?: return emptyList()
        return runCatching {
            json.decodeFromString<List<String>>(raw)
        }.getOrElse { emptyList() }
    }

    fun saveName(name: String) {
        val trimmed = name.trim()
        if (trimmed.isBlank()) return

        val updated = buildList {
            add(trimmed)
            addAll(getHistory().filterNot { it.equals(trimmed, ignoreCase = true) })
        }.take(maxItems)

        settings.putString(key, json.encodeToString(updated))
    }

    fun removeName(name: String) {
        val updated = getHistory().filterNot { it.equals(name, ignoreCase = true) }
        settings.putString(key, json.encodeToString(updated))
    }

    fun clearHistory() {
        settings.remove(key)
    }
}

data class NameHistoryState(
    val query: TextFieldValue = TextFieldValue(""),
    val history: List<String> = emptyList()
)


//Helper
fun saveNameToHistory(
    current: String,
    history: List<String>,
    maxItems: Int = 10
): List<String> {
    val trimmed = current.trim()
    if (trimmed.isBlank()) return history

    return buildList {
        add(trimmed)
        addAll(history.filterNot { it.equals(trimmed, ignoreCase = true) })
    }.take(maxItems)
}