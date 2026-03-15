package ngo.friendship.mhealth.dc.presentation.screens.main.case

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ngo.friendship.mhealth.dc.domain.model.InterviewDetails
import ngo.friendship.mhealth.dc.domain.repository.InterviewDetailsRepository
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel
import ngo.friendship.mhealth.dc.presentation.state.RequestState

class InterviewDetailsViewModel(
    private val repository: InterviewDetailsRepository
) : BaseViewModel() {
    private val _interviewDetailsState =
        MutableStateFlow<RequestState<InterviewDetails>>(RequestState.Idle)
    val interviewDetailsState: StateFlow<RequestState<InterviewDetails>> = _interviewDetailsState

    fun loadInterviewDetails(
        userName: String,
        password: String,
        interviewId: Long
    ) {
        launch {
            _interviewDetailsState.value = RequestState.Loading
            runCatching {
                repository.getInterviewDetails(
                    userName = userName,
                    password = password,
                    interviewId = interviewId
                )
            }.onSuccess { result ->
                _interviewDetailsState.value = RequestState.Success(data = result)
            }.onFailure { throwable ->
                _interviewDetailsState.value = RequestState.Error(
                    throwable.message ?: "Failed to load interview"
                )
            }
        }

    }

}