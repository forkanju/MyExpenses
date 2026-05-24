package ngo.friendship.mhealth.dc.presentation.base

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ngo.friendship.mhealth.dc.utils.currentTimestamp

enum class SnackbarType {
    SUCCESS, ERROR, WARNING, DEFAULT
}

class ColoredSnackbarVisuals(
    override val message: String,
    override val actionLabel: String? = null,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    override val withDismissAction: Boolean = false,
    val type: SnackbarType = SnackbarType.DEFAULT
) : SnackbarVisuals

data class SnackbarEvent(
    val message: String,
    val type: SnackbarType = SnackbarType.DEFAULT,
    val action: SnackbarAction? = null
)

data class SnackbarAction(
    val label: String,
    val onAction: suspend () -> Unit
)

object SnackbarController {
    private val _events = Channel<SnackbarEvent>()
    val events = _events.receiveAsFlow()

    private const val SNACKBAR_DEBOUNCE_MS = 4000L // 4 seconds is the default debounce time for snackbars

    private var lastSnackbarTimestamp: Long = 0L

    fun sendEvent(
        message: String,
        type: SnackbarType = SnackbarType.DEFAULT,
        actionLabel: String? = null,
        onAction: suspend () -> Unit = {}
    ) {
        val now = currentTimestamp
        if (currentTimestamp - lastSnackbarTimestamp < SNACKBAR_DEBOUNCE_MS) {
            return
        }
        lastSnackbarTimestamp = now

        MainScope().launch {
            _events.send(
                SnackbarEvent(
                    message = message,
                    type = type,
                    action = actionLabel?.let {
                        SnackbarAction(
                            label = actionLabel,
                            onAction = onAction
                        )
                    }
                )
            )
        }
    }
}

@Composable
fun <T> ObserveAsEvents(
    flow: Flow<T>,
    key1: Any? = null,
    key2: Any? = null,
    onEvent: suspend CoroutineScope.(T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner.lifecycle, key1, key2, flow) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect {
                    onEvent(it)
                }
            }
        }
    }
}