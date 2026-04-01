package ngo.friendship.mhealth.dc.domain.repository

import ngo.friendship.mhealth.dc.domain.model.SaveDoctorFeedbackResult
import ngo.friendship.mhealth.dc.presentation.screens.main.case.save_feedback.DoctorFeedbackFormState

interface SaveDoctorFeedbackRepository {
    suspend fun saveDoctorFeedback(
        userName: String,
        password: String,
        formState: DoctorFeedbackFormState
    ): SaveDoctorFeedbackResult
}