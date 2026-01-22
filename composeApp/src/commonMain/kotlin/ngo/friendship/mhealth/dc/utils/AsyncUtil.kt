package ngo.friendship.mhealth.dc.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

context(scope: CoroutineScope)
fun <T> Iterable<T>.forEachAsync(action: suspend CoroutineScope.(T) -> Unit) {
    this.forEach {
        scope.launch {
            action(it)
        }
    }
}

suspend fun <T, R> Iterable<T>.mapAsync(action: suspend CoroutineScope.(T) -> R): List<R> {
    return coroutineScope {
        this@mapAsync.map {
            async {
                action(it)
            }
        }.awaitAll()
    }
}