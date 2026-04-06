package ngo.friendship.mhealth.dc.presentation.screens.case

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ngo.friendship.mhealth.dc.domain.model.InterviewDetails
import ngo.friendship.mhealth.dc.domain.model.Medicine
import ngo.friendship.mhealth.dc.domain.model.QuestionAnswerJson
import ngo.friendship.mhealth.dc.domain.repository.CaseRepository
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel
import ngo.friendship.mhealth.dc.presentation.base.SnackbarController
import ngo.friendship.mhealth.dc.presentation.screens.case.prescription_form.model.DoctorFeedbackFormState

class CaseViewModel(
    private val repository: CaseRepository
) : BaseViewModel() {

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
        launch(onEnd = {
            if (interviewDetailsState.value.interviewId == -1L)
                backStack.removeLastOrNull()
        }) {
            interviewDetailsState.value = InterviewDetails()
            interviewDetailsState.value = repository.getInterviewDetails(interviewId = interviewId)
        }
    }

    fun loadQuestionAnswerData(interviewId: Long) {
        launch {
            val result = repository.getQuestionAnswerData()

            questionAnswerState.value = result
            println("TTTT LOAD q1 size = ${result.questionAnswerJson.size}")
            println("TTTT LOAD q2 size = ${result.questionAnswerJson2.size}")
            formState.value = formState.value.copy(
                interviewId = interviewId,
                questionAnswers = result.questionAnswerJson,
                questionAnswers2 = result.questionAnswerJson2
            )
        }
    }

    fun loadMedicineList(
        type: String = "Tab"
    ) {
        launch(loading = Loading.Gone) {
            medicineListState.value = repository.getMedicineList(type = type)
        }
    }

    fun saveDoctorFeedback() {
        launch {
            repository.saveDoctorFeedback(formState = formState.value)
            showSuccess("Feedback saved successfully")
            backStack.removeLastOrNull()
        }
    }

    fun updateFormState(state: DoctorFeedbackFormState) {
        formState.value = state
    }

}