package ngo.friendship.mhealth.dc.presentation.screens.main.case

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ngo.friendship.mhealth.dc.domain.model.SaveDoctorFeedbackResult
import ngo.friendship.mhealth.dc.domain.repository.SaveDoctorFeedbackRepository
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel
import ngo.friendship.mhealth.dc.presentation.screens.main.case.save_feedback.DoctorFeedbackFormState
import ngo.friendship.mhealth.dc.presentation.state.RequestState

class SaveDoctorFeedbackViewModel(
    private val repository: SaveDoctorFeedbackRepository
): BaseViewModel() {
    private val _state =
        MutableStateFlow<RequestState<SaveDoctorFeedbackResult>>(RequestState.Idle)
    val state: StateFlow<RequestState<SaveDoctorFeedbackResult>> = _state

    fun saveDoctorFeedback(
        userName: String,
        password: String,
        formState: DoctorFeedbackFormState
    ) {
        launch {
            _state.value = RequestState.Loading
            runCatching {
                repository.saveDoctorFeedback(
                    userName = userName,
                    password = password,
                    formState = formState
                )
            }.onSuccess {
                _state.value = RequestState.Success(it)
            }.onFailure {
                it.printStackTrace()
                _state.value = RequestState.Error(it.message ?: "Error")
            }
        }
    }

    fun clearState() {
        _state.value = RequestState.Idle
    }
}