package ngo.friendship.mhealth.dc

import android.app.Application
import ngo.friendship.mhealth.dc.domain.network.ConnectionListener
import ngo.friendship.mhealth.dc.utils.log

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ConnectionListener.isConnected.log("MyApplication")
        CMPApplication(
            isDebug = BuildConfig.DEBUG,
            applicationContext = applicationContext
        )
        AppShortcuts.setupKtorMonitorShortcut(applicationContext)
    }
}