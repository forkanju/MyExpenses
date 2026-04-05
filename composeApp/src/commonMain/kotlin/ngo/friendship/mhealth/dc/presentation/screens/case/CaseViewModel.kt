package ngo.friendship.mhealth.dc.presentation.screens.case

import kotlinx.coroutines.flow.MutableStateFlow
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

    val interviewDetailsState: StateFlow<InterviewDetails>
        field = MutableStateFlow<InterviewDetails>(InterviewDetails())

    val medicineListState: StateFlow<List<Medicine>>
        field = MutableStateFlow<List<Medicine>>(listOf())

    val questionAnswerState: StateFlow<QuestionAnswerJson>
        field = MutableStateFlow<QuestionAnswerJson>(QuestionAnswerJson())

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


    fun saveDoctorFeedback(formState: DoctorFeedbackFormState) {
        launch {
            repository.saveDoctorFeedback(formState = formState)
            backStack.removeLastOrNull()
        }
    }

    fun loadQuestionAnswerData() {
        launch {
            questionAnswerState.value = repository.getQuestionAnswerData()
        }
    }

}