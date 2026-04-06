package ngo.friendship.mhealth.dc.presentation.screens.case

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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


//    fun saveDoctorFeedback(formState: DoctorFeedbackFormState) {
//        launch {
//            repository.saveDoctorFeedback(formState = formState)
//            backStack.removeLastOrNull()
//        }
//    }

    fun saveDoctorFeedback(formState: DoctorFeedbackFormState) {
        launch {
            // 🔍 DEBUG PRINT HERE
            println("TTTT VM _formState q1 size = ${_formState.value.questionAnswers.size}")
            println("TTTT VM _formState q2 size = ${_formState.value.questionAnswers2.size}")
            println("TTTT UI formState q1 size = ${formState.questionAnswers.size}")
            println("TTTT UI formState q2 size = ${formState.questionAnswers2.size}")
            val finalState = formState.copy(
                interviewId = formState.interviewId ?: _formState.value.interviewId,
                questionAnswers = _formState.value.questionAnswers,
                questionAnswers2 = _formState.value.questionAnswers2
            )

            // 🔍 AFTER MERGE CHECK
            println("TTTT FINAL q1 size = ${finalState.questionAnswers.size}")
            println("TTTT FINAL q2 size = ${finalState.questionAnswers2.size}")
            repository.saveDoctorFeedback(formState = finalState)
//            backStack.removeLastOrNull()
        }
    }

    fun loadQuestionAnswerData() {
        launch {
            val result = repository.getQuestionAnswerData()

            questionAnswerState.value = result
            println("TTTT LOAD q1 size = ${result.questionAnswerJson.size}")
            println("TTTT LOAD q2 size = ${result.questionAnswerJson2.size}")
            _formState.value = _formState.value.copy(
                questionAnswers = result.questionAnswerJson,
                questionAnswers2 = result.questionAnswerJson2
            )
        }
    }

}