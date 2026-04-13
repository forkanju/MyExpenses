package ngo.friendship.mhealth.dc.fcm

import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.PayloadData
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import ngo.friendship.mhealth.dc.isDebugBuild

//
//class AppNotifierManager {
//    // Initialize the push notifier instance
//    private val pushNotifier = NotifierManager.getPushNotifier()
//
//
//    // একটি গ্লোবাল ইভেন্ট ফ্লো যা সারা অ্যাপ থেকে এক্সেস করা যাবে
//    private val _notificationClickFlow = MutableSharedFlow<PayloadData>(extraBufferCapacity = 1)
//    val notificationClickFlow = _notificationClickFlow.asSharedFlow()
//
//    init {
//        if (isDebugBuild) {
//            setLogger()
//            MainScope().launch {
//                subscribeToTopic(FcmTopics.TEST)
//            }
//        }
//    }
//
//    // Subscribe to a topic
//    suspend fun subscribeToTopic(topic: String) {
//        pushNotifier.subscribeToTopic(topic)
//        println("Subscribed to topic: $topic")
//    }
//
//    // Unsubscribe from a topic
//    suspend fun unsubscribeFromTopic(topic: String) {
//        pushNotifier.unSubscribeFromTopic(topic)
//        println("Unsubscribed from topic: $topic")
//    }
//
//    // Get current push notification token
//    suspend fun getDeviceToken(): String? {
//        return pushNotifier.getToken()
//    }
//
//    // Delete current push notification token (e.g., for logging out the user)
//    suspend fun deleteDeviceToken() {
//        pushNotifier.deleteMyToken()
//        println("Token deleted successfully")
//    }
//
//    // Set a custom logger for internal logging of the library
//    fun setLogger() {
//        NotifierManager.setLogger { message ->
//            // Log the message to the console (or any other logging system you prefer)
//            println("KMPNotifier Log: $message")
//        }
//    }
//
//    // Add listener for notification events with a callback
//    fun addListener(onNotificationClickedCallback: (PayloadData) -> Unit) {
//        println("AppNotifierManager: listener registered")
//        NotifierManager.addListener(object : NotifierManager.Listener {
//            override fun onNotificationClicked(data: PayloadData) {
//                super.onNotificationClicked(data)
//                println("AppNotifierManager: notification clicked = $data")
//                onNotificationClickedCallback(data)
//            }
//        })
//    }
//}


class AppNotifierManager {

    private val pushNotifier = NotifierManager.getPushNotifier()

    val notificationClickFlow: SharedFlow<PayloadData>
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