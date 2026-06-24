package ngo.friendship.mhealth.dc.domain.model

enum class NetworkStatus {
    ONLINE,      // Internet OK, API Success (Green)
    API_ERROR,   // Internet OK, API Error (Yellow)
    OFFLINE      // No Internet (Red)
}
