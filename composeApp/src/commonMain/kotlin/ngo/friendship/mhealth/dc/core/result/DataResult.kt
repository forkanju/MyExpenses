package ngo.friendship.mhealth.dc.core.result

sealed class DataResult<out T> {
    data class Success<out T>(val data: T) : DataResult<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : DataResult<Nothing>()
    data object Loading : DataResult<Nothing>()
}
