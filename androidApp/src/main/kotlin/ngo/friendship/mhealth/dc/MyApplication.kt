package ngo.friendship.mhealth.dc

import android.app.Application
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import ngo.friendship.mhealth.dc.domain.network.ConnectionListener
import ngo.friendship.mhealth.dc.utils.log

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ConnectionListener.isConnected.log("MyApplication")
        NotifierManager.initialize(
            configuration = NotificationPlatformConfiguration.Android(
                notificationIconResId = R.drawable.ic_launcher_foreground,
                showPushNotification = true,
            )
        )
        CMPApplication(
            isDebug = BuildConfig.DEBUG,
            applicationContext = applicationContext
        )
        AppShortcuts.setupKtorMonitorShortcut(applicationContext)
    }
}