package ngo.friendship.mhealth.dc

class CMPApplication(isDebug: Boolean, applicationContext: Any) {
    init {
        CMPApplication.isDebug = isDebug
        context = applicationContext
    }
    companion object {
        var isDebug = true
        lateinit var context: Any
    }
}