package ngo.friendship.mhealth.dc.domain.repository

import ngo.friendship.mhealth.dc.domain.model.Interview
import ngo.friendship.mhealth.dc.domain.model.InterviewDetails
import ngo.friendship.mhealth.dc.domain.model.Medicine
import ngo.friendship.mhealth.dc.domain.model.QuestionAnswerJson
import ngo.friendship.mhealth.dc.domain.model.SaveDoctorFeedbackResult
import ngo.friendship.mhealth.dc.presentation.screens.main.prescription_form.model.DoctorFeedbackFormState

interface PrescriptionFormRepository {

    suspend fun getInterviewList(appVersion: Int): List<Interview>

    suspend fun getInterviewDetails(interviewId: Long): InterviewDetails

    suspend fun getMedicineList(type: String): List<Medicine>

    suspend fun saveDoctorFeedback(formState: DoctorFeedbackFormState): SaveDoctorFeedbackResult

    suspend fun getQuestionAnswerData(): QuestionAnswerJson
}