package ngo.friendship.mhealth.dc.presentation.screens.main.case

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ngo.friendship.mhealth.dc.domain.model.QuestionAnswerJson
import ngo.friendship.mhealth.dc.domain.repository.QuestionAnswerJsonRepository
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel
import ngo.friendship.mhealth.dc.presentation.state.RequestState

class QuestionAnswerJsonViewModel(
    private val repository: QuestionAnswerJsonRepository
) : BaseViewModel() {

    val state : StateFlow<RequestState<QuestionAnswerJson>>
        field = MutableStateFlow<RequestState<QuestionAnswerJson>>(RequestState.Idle)

    fun loadQuestionAnswerData(
        userName: String,
        password: String
    ) {
        launch {
            state.value = RequestState.Loading

            runCatching {
                repository.getQuestionAnswerData(
                    userName = userName,
                    password = password
                )
            }.onSuccess { result ->
                state.value = RequestState.Success(result)
            }.onFailure { throwable ->
                state.value = RequestState.Error(
                    throwable.message ?: "Failed to load question answer data"
                )
            }
        }
    }

    fun resetState() {
        state.value = RequestState.Idle
    }
}