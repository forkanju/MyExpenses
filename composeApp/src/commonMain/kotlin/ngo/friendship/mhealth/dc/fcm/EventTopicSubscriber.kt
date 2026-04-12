package ngo.friendship.mhealth.dc.fcm

expect class EventTopicSubscriber() {
    suspend fun subscribeToTopic(topic: String): Boolean
    suspend fun unsubscribeFromTopic(topic: String): Boolean
}