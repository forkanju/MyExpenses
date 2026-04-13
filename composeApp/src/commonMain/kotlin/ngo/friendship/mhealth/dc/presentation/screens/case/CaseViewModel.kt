package ngo.friendship.mhealth.dc.presentation.screens.case

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import ngo.friendship.mhealth.dc.domain.model.InterviewDetails
import ngo.friendship.mhealth.dc.domain.model.Medicine
import ngo.friendship.mhealth.dc.domain.model.QuestionAnswerJson
import ngo.friendship.mhealth.dc.domain.repository.CaseRepository
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel
import ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.model.DoctorFeedbackFormState

class CaseViewModel(
    private val repository: CaseRepository
) : BaseViewModel() {

    val uiEvent: SharedFlow<CaseUiEvent>
        field = MutableSharedFlow(replay = 1)
    val interviewDetailsState: StateFlow<InterviewDetails>
        field = MutableStateFlow(InterviewDetails())

    val medicineListState: StateFlow<List<Medicine>>
        field = MutableStateFlow(listOf())

    val questionAnswerState: StateFlow<QuestionAnswerJson>
        field = MutableStateFlow(QuestionAnswerJson())

    val formState: StateFlow<DoctorFeedbackFormState>
        field = MutableStateFlow(DoctorFeedbackFormState())

    init {
        loadMedicineList()
    }

    fun loadInterviewDetails(interviewId: Long) {
        launch {
            interviewDetailsState.value = repository.getInterviewDetails(interviewId = interviewId)
        }
    }

    fun loadMedicineList(
        type: String = "Tab"
    ) {
        launch(loading = Loading.Gone) {
            medicineListState.value = repository.getMedicineList(type = type)
        }
    }


    fun saveDoctorFeedback(state: DoctorFeedbackFormState, mobile: String, sms: String) {
        launch {
            val finalState = state.copy(
                interviewId = state.interviewId ?: formState.value.interviewId,
                questionAnswers = formState.value.questionAnswers,
                questionAnswers2 = formState.value.questionAnswers2
            )

            try {
                repository.saveDoctorFeedback(formState = finalState)

                val interviewId = finalState.interviewId
                if (interviewId != null) {
                    val isStatusUpdated = repository.updateInterviewStatus(
                        interviewId = interviewId,
                        status = "Close"
                    )

                    if (!isStatusUpdated) {
                        uiEvent.emit(
                            CaseUiEvent.ShowSnackbar("Feedback saved, but status update failed")
                        )
                        return@launch
                    }
                }

                repository.sendSms(mobile, sms);
                uiEvent.emit(CaseUiEvent.ShowSnackbar("Feedback saved successfully"))
                uiEvent.emit(CaseUiEvent.NavigateBack)

            } catch (e: Exception) {
                uiEvent.emit(
                    CaseUiEvent.ShowSnackbar(

                        e.message ?: "Failed to save feedback"
                    )
                )
            }
        }
    }

    fun loadQuestionAnswerData() {
        launch {
            val result = repository.getQuestionAnswerData()
            questionAnswerState.value = result
            formState.value = formState.value.copy(
                questionAnswers = result.questionAnswerJson,
                questionAnswers2 = result.questionAnswerJson2
            )
        }
    }

}