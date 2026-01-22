package ngo.friendship.mhealth.dc.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ngo.friendship.mhealth.dc.utils.log
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseViewModel: ViewModel() {

    open lateinit var backStack : NavBackStack<NavKey>

    val errorFlow = MutableSharedFlow<Info>()
    val successFlow = MutableSharedFlow<Info>()
    val loadingFlow = MutableStateFlow(false)
    val loadingSecondaryFlow = MutableStateFlow(false)

    protected val mainContext: CoroutineContext = Dispatchers.Main

    protected open var defaultErrorHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.log()
        sendError(throwable)
    }

    protected fun launch(
        dispatcher: CoroutineContext = EmptyCoroutineContext,
        loading: Loading = Loading.Primary,
        coroutineExceptionHandler: CoroutineExceptionHandler = defaultErrorHandler,
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

    fun clearError(){
        launch(mainContext, loading = Loading.Gone) {
            errorFlow.emit(Info())
        }
    }

    fun clearSuccess(){
        launch(mainContext, loading = Loading.Gone) {
            successFlow.emit(Info())
        }
    }

    enum class Loading {
        Primary,
        Secondary,
        Gone
    }
}