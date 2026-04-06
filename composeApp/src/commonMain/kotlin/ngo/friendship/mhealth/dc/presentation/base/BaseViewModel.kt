package ngo.friendship.mhealth.dc.presentation.base

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.utils.log
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseViewModel: ViewModel() {

    open lateinit var backStack : NavBackStack<NavKey>
    lateinit var snackBarState : SnackbarHostState

    fun setSnackHostBarState(snackBarState: SnackbarHostState){
        if (!::snackBarState.isInitialized)
            this.snackBarState = snackBarState
    }

    val errorFlow = MutableSharedFlow<Info>()
    val successFlow = MutableSharedFlow<Info>()
    val loadingFlow = MutableStateFlow(false)
    val loadingSecondaryFlow = MutableStateFlow(false)

    protected val mainContext: CoroutineContext = Dispatchers.Main

    protected open var defaultErrorHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.log()
        sendError(throwable)
    }

    init {
        CoroutineScope(Dispatchers.Main.immediate).launch {
            errorFlow.collect {
                if (it.message.isBlank() || it.type != Info.Type.Default) return@collect
                snackBarState.currentSnackbarData?.dismiss()
                if (it.message.length > 50)
                    backStack.add(Screens.Dialog.Error(it.message))
                else
                    snackBarState.showSnackbar(it.message)
            }
        }
        CoroutineScope(Dispatchers.Main.immediate).launch {
            successFlow.collect {
                if (it.message.isBlank() || it.type != Info.Type.Default) return@collect
                snackBarState.currentSnackbarData?.dismiss()
                snackBarState.showSnackbar(it.message)
            }
        }
    }

    fun listenSnackbarControllerEvents() {
        CoroutineScope(Dispatchers.Main.immediate).launch {
            SnackbarController.events.collect {
                if (it.message.isBlank()) return@collect
                snackBarState.currentSnackbarData?.dismiss()
                val result = snackBarState.showSnackbar(
                    message = it.message,
                    actionLabel = it.action?.label,
                    withDismissAction = it.action?.onAction != null,
                )
                if (result == SnackbarResult.ActionPerformed)
                    it.action?.onAction()
            }
        }
    }

    protected fun launch(
        dispatcher: CoroutineContext = EmptyCoroutineContext,
        loading: Loading = Loading.Primary,
        coroutineExceptionHandler: CoroutineExceptionHandler = defaultErrorHandler,
        onEnd: (() -> Unit) = {},
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return viewModelScope.launch(dispatcher + coroutineExceptionHandler) {
            val loadingJob = launch {
                delay(200)
                setLoading(true, loading)
            }
            try {
                block()
            } finally {
                loadingJob.cancel()
                setLoading(false, loading)
                onEnd()
            }
        }
    }

    fun setLoading(value: Boolean, loading: Loading = Loading.Primary) {
        when(loading){
            Loading.Primary -> loadingFlow.value = value
            Loading.Secondary -> loadingSecondaryFlow.value = value
            Loading.Gone -> Unit
        }
    }

    fun showError(message: String) {
        launch(mainContext, loading = Loading.Gone) {
            errorFlow.emit(Info(message))
        }
    }

    fun showSuccess(message: String){
        launch(mainContext, loading = Loading.Gone) {
            successFlow.emit(Info(message))
        }
    }

    protected fun sendError(throwable: Throwable) {
        launch(mainContext, loading = Loading.Gone) {
            if(throwable.message?.equals("null", true) == true) return@launch
            throwable.message?.let {
                errorFlow.emit(Info(it))
            }
        }
    }

    enum class Loading {
        Primary,
        Secondary,
        Gone
    }
}