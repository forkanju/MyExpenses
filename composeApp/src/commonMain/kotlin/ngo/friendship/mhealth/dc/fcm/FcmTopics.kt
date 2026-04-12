package ngo.friendship.mhealth.dc.fcm

object FcmTopics {
    const val TEST = "test"
    const val CASE_LIST_UPDATES = "case_list_updates"
    const val INTERVIEW_UPDATES = "interview_updates"
    const val PRESCRIPTION_UPDATES = "prescription_updates"

    fun doctorCaseList(doctorId: String): String = "doctor_${doctorId}_case_list"
    fun doctorInterviewUpdates(doctorId: String): String = "doctor_${doctorId}_interview_updates"
}