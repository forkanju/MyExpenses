package ngo.friendship.mhealth.dc

import android.app.Application

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CMPApplication(
            isDebug = BuildConfig.DEBUG,
            applicationContext = applicationContext
        )
        AppShortcuts.setupKtorMonitorShortcut(applicationContext)
    }
}