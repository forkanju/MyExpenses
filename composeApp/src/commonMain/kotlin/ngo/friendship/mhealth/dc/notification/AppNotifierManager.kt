package ngo.friendship.mhealth.dc.notification

import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.PayloadData
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import ngo.friendship.mhealth.dc.isDebugBuild

class AppNotifierManager {

    private val pushNotifier = NotifierManager.getPushNotifier()

    val notificationClickFlow: SharedFlow<PayloadData>
        field = MutableSharedFlow(extraBufferCapacity = 1)

    val notificationReceivedFlow: SharedFlow<PayloadData>
        field = MutableSharedFlow(extraBufferCapacity = 1)

    init {
        if (isDebugBuild) {
            setLogger()
            MainScope().launch {
                subscribeToTopic(FcmTopics.TEST)
            }
        }

        println("AppNotifierManager: Global listener initialized")

        NotifierManager.setListener(object : NotifierManager.Listener {
            override fun onNotificationClicked(data: PayloadData) {
                println("AppNotifierManager: notification clicked = $data")
                MainScope().launch {
                    notificationClickFlow.emit(data)
                }
            }

            override fun onPayloadData(data: PayloadData) {
                println("AppNotifierManager: notification payload received = $data")
                MainScope().launch {
                    notificationReceivedFlow.emit(data)
                }
            }
        })
    }

    suspend fun subscribeToTopic(topic: String) {
        pushNotifier.subscribeToTopic(topic)
        println("Subscribed to topic: $topic")
    }

    suspend fun unsubscribeFromTopic(topic: String) {
        pushNotifier.unSubscribeFromTopic(topic)
        println("Unsubscribed from topic: $topic")
    }

    suspend fun getDeviceToken(): String? {
        val token = pushNotifier.getToken()
        println("Token: $token")
        return pushNotifier.getToken()
    }

    suspend fun deleteDeviceToken() {
        pushNotifier.deleteMyToken()
        println("Token deleted successfully")
    }

    fun setLogger() {
        NotifierManager.setLogger { message ->
            println("KMPNotifier Log: $message")
        }
    }
}