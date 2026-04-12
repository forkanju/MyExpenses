package ngo.friendship.mhealth.dc.fcm

import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.PayloadData
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ngo.friendship.mhealth.dc.isDebugBuild

class NotifierManager {
    // Initialize the push notifier instance
    private val pushNotifier = NotifierManager.getPushNotifier()

    init {
        if (isDebugBuild) {
            setLogger()
            MainScope().launch {
                subscribeToTopic(FcmTopics.TEST)
            }
        }
    }

    // Subscribe to a topic
    suspend fun subscribeToTopic(topic: String) {
        pushNotifier.subscribeToTopic(topic)
        println("Subscribed to topic: $topic")
    }

    // Unsubscribe from a topic
    suspend fun unsubscribeFromTopic(topic: String) {
        pushNotifier.unSubscribeFromTopic(topic)
        println("Unsubscribed from topic: $topic")
    }

    // Get current push notification token
    suspend fun getDeviceToken(): String? {
        return pushNotifier.getToken()
    }

    // Delete current push notification token (e.g., for logging out the user)
    suspend fun deleteDeviceToken() {
        pushNotifier.deleteMyToken()
        println("Token deleted successfully")
    }

    // Set a custom logger for internal logging of the library
    fun setLogger() {
        NotifierManager.setLogger { message ->
            // Log the message to the console (or any other logging system you prefer)
            println("KMPNotifier Log: $message")
        }
    }

    // Add listener for notification events with a callback
    fun addListener(onNotificationClickedCallback: (PayloadData) -> Unit) {
        NotifierManager.addListener(object : NotifierManager.Listener {
            override fun onNotificationClicked(data: PayloadData) {
                super.onNotificationClicked(data)
                // Invoke the callback when the notification is clicked
                onNotificationClickedCallback(data)
            }
        })
    }
}