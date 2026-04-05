package ngo.friendship.mhealth.dc.domain.network

import dev.jordond.connectivity.Connectivity
import dev.jordond.connectivity.ConnectivityOptions
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import ngo.friendship.mhealth.dc.utils.log

object ConnectionListener {
    const val TAG = "ConnectionListener"

    private val connectivity = Connectivity(options = ConnectivityOptions(true))

    val connectionStatusFlow = connectivity.statusUpdates.onEach {
        it.log(TAG)
    }.stateIn(
        MainScope(),
        initialValue = Connectivity.Status.Disconnected,
        started = SharingStarted.Eagerly
    )

    val isConnected
        get() = connectionStatusFlow.value.isConnected

    fun unregisterListener() {
        connectivity.stop()
    }
}