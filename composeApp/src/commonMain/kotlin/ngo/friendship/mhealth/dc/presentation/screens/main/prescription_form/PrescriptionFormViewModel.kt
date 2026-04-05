package ngo.friendship.mhealth.dc.presentation.screens.main.prescription_form

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ngo.friendship.mhealth.dc.domain.model.InterviewDetails
import ngo.friendship.mhealth.dc.domain.model.Medicine
import ngo.friendship.mhealth.dc.domain.model.QuestionAnswerJson
import ngo.friendship.mhealth.dc.domain.repository.PrescriptionFormRepository
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel
import ngo.friendship.mhealth.dc.presentation.screens.main.prescription_form.model.DoctorFeedbackFormState

class PrescriptionFormViewModel(
    private val repository: PrescriptionFormRepository
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


    fun saveDoctorFeedback(formState: DoctorFeedbackFormState, onSuccess: () -> Unit = {}) {
        launch {
            repository.saveDoctorFeedback(formState = formState)
            onSuccess()
            backStack.removeLastOrNull()
        }
    }

    fun loadQuestionAnswerData() {
        launch {
            questionAnswerState.value = repository.getQuestionAnswerData()
        }
    }

}