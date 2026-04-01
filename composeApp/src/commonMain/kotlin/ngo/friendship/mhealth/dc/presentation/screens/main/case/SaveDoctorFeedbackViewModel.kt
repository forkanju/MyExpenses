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
        println("SAVE_API: ViewModel called") // 👈 ADD THIS
//        launch {
//            println("SAVE_API: inside coroutine") // 👈 ADD THIS
//            _state.value = RequestState.Loading
//
//            runCatching {
//                repository.saveDoctorFeedback(userName, password, formState)
//            }.onSuccess { result ->
//                println("SAVE_API: success from repo") // 👈 ADD
//                _state.value = RequestState.Success(result)
//            }.onFailure { throwable ->
//                println("SAVE_API: error ${throwable.message}") // 👈 ADD
//                _state.value = RequestState.Error(
//                    throwable.message ?: "Save failed"
//                )
//            }
//        }

        launch {
            println("SAVE_API: inside launch block START")

            _state.value = RequestState.Loading

            runCatching {
                println("SAVE_API: before repository call")

                repository.saveDoctorFeedback(
                    userName = userName,
                    password = password,
                    formState = formState
                )

            }.onSuccess {
                println("SAVE_API: repository success")

                _state.value = RequestState.Success(it)
            }.onFailure {
                println("SAVE_API: ERROR = ${it.message}")
                it.printStackTrace()   // 🔥 VERY IMPORTANT

                _state.value = RequestState.Error(it.message ?: "Error")
            }

            println("SAVE_API: inside launch block END")
        }
    }
}