package ngo.friendship.mhealth.dc.domain.network

import ngo.friendship.mhealth.dc.domain.network.ConnectionListener.Companion.instance
import dev.jordond.connectivity.Connectivity
import dev.jordond.connectivity.ConnectivityOptions
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import ngo.friendship.mhealth.dc.utils.log

class ConnectionListener() {
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

    init {
        instance = this
    }

    fun unregisterListener() {
        connectivity.stop()
    }

    companion object {
        const val TAG = "ConnectionListener"
        var instance: ConnectionListener? = null
    }
}

val connectionListener by lazy { instance ?: ConnectionListener() }
