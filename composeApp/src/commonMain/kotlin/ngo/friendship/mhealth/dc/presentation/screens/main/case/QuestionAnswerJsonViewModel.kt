package ngo.friendship.mhealth.dc.presentation.screens.main.case

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ngo.friendship.mhealth.dc.domain.model.GateQuestionAnswerData
import ngo.friendship.mhealth.dc.domain.repository.QuestionAnswerJsonRepository
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel
import ngo.friendship.mhealth.dc.presentation.state.RequestState

class QuestionAnswerJsonViewModel(
    private val repository: QuestionAnswerJsonRepository
) : BaseViewModel() {

    private val _state =
        MutableStateFlow<RequestState<GateQuestionAnswerData>>(RequestState.Idle)
    val state: StateFlow<RequestState<GateQuestionAnswerData>> = _state

    fun loadQuestionAnswerData(
        userName: String,
        password: String
    ) {
        launch {
            _state.value = RequestState.Loading

            runCatching {
                repository.getQuestionAnswerData(
                    userName = userName,
                    password = password
                )
            }.onSuccess { result ->
                _state.value = RequestState.Success(result)
            }.onFailure { throwable ->
                _state.value = RequestState.Error(
                    throwable.message ?: "Failed to load question answer data"
                )
            }
        }
    }

    fun resetState() {
        _state.value = RequestState.Idle
    }
}