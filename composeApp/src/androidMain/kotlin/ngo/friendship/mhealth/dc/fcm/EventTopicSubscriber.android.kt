package ngo.friendship.mhealth.dc.fcm

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

actual class EventTopicSubscriber actual constructor(){
    actual suspend fun subscribeToTopic(topic: String): Boolean {
        return suspendCancellableCoroutine { cont ->
            FirebaseMessaging.getInstance()
                .subscribeToTopic(topic)
                .addOnCompleteListener { task ->
                    cont.resume(task.isSuccessful)
                }
        }
    }

    actual suspend fun unsubscribeFromTopic(topic: String): Boolean {
        return suspendCancellableCoroutine { cont ->
            FirebaseMessaging.getInstance()
                .unsubscribeFromTopic(topic)
                .addOnCompleteListener { task ->
                    cont.resume(task.isSuccessful)
                }
        }
    }


}