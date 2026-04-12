package ngo.friendship.mhealth.dc.presentation.screens.case

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import ngo.friendship.mhealth.dc.domain.model.InterviewDetails
import ngo.friendship.mhealth.dc.domain.model.Medicine
import ngo.friendship.mhealth.dc.domain.model.QuestionAnswerJson
import ngo.friendship.mhealth.dc.domain.repository.CaseRepository
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel
import ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.model.DoctorFeedbackFormState

class CaseViewModel(
    private val repository: CaseRepository
) : BaseViewModel() {
    private val _uiEvent = MutableSharedFlow<CaseUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    val interviewDetailsState: StateFlow<InterviewDetails>
        field = MutableStateFlow<InterviewDetails>(InterviewDetails())

    val medicineListState: StateFlow<List<Medicine>>
        field = MutableStateFlow<List<Medicine>>(listOf())

    val questionAnswerState: StateFlow<QuestionAnswerJson>
        field = MutableStateFlow<QuestionAnswerJson>(QuestionAnswerJson())

    private val _formState = MutableStateFlow(DoctorFeedbackFormState())
    val formState = _formState.asStateFlow()

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


    fun saveDoctorFeedback(formState: DoctorFeedbackFormState, mobile: String, sms: String) {
        launch {
            val finalState = formState.copy(
                interviewId = formState.interviewId ?: _formState.value.interviewId,
                questionAnswers = _formState.value.questionAnswers,
                questionAnswers2 = _formState.value.questionAnswers2
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
                        _uiEvent.emit(
                            CaseUiEvent.ShowSnackbar("Feedback saved, but status update failed")
                        )
                        return@launch
                    }
                }

                repository.sendSms(mobile, sms);
                _uiEvent.emit(CaseUiEvent.ShowSnackbar("Feedback saved successfully"))
                _uiEvent.emit(CaseUiEvent.NavigateBack)

            } catch (e: Exception) {
                _uiEvent.emit(
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
            _formState.value = _formState.value.copy(
                questionAnswers = result.questionAnswerJson,
                questionAnswers2 = result.questionAnswerJson2
            )
        }
    }

}