package ngo.friendship.mhealth.dc.presentation.screens.main.case

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ngo.friendship.mhealth.dc.domain.model.SaveDoctorFeedbackResult
import ngo.friendship.mhealth.dc.domain.repository.SaveDoctorFeedbackRepository
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel
import ngo.friendship.mhealth.dc.presentation.screens.main.case.save_feedback.DoctorFeedbackFormState
import ngo.friendship.mhealth.dc.presentation.state.RequestState

class SaveDoctorFeedbackViewModel(
    private val repository: SaveDoctorFeedbackRepository
): BaseViewModel() {

    val state : StateFlow<RequestState<SaveDoctorFeedbackResult>>
        field = MutableStateFlow<RequestState<SaveDoctorFeedbackResult>>(RequestState.Idle)
    

    fun saveDoctorFeedback(
        userName: String,
        password: String,
        formState: DoctorFeedbackFormState
    ) {
        launch {
            state.value = RequestState.Loading
            runCatching {
                repository.saveDoctorFeedback(
                    userName = userName,
                    password = password,
                    formState = formState
                )
            }.onSuccess {
                state.value = RequestState.Success(it)
            }.onFailure {
                it.printStackTrace()
                state.value = RequestState.Error(it.message ?: "Error")
            }
        }
    }

    fun clearState() {
        state.value = RequestState.Idle
    }
}