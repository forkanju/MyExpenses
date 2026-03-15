package ngo.friendship.mhealth.dc.presentation.screens.main.case

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ngo.friendship.mhealth.dc.domain.model.Interview
import ngo.friendship.mhealth.dc.domain.repository.InterviewListRepository
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel

class InterviewListViewModel(
    private val repository: InterviewListRepository
) : BaseViewModel() {

    val interviewListState: StateFlow<List<Interview>>
        field = MutableStateFlow(emptyList())

    fun loadInterviewList(userName: String, password: String, appVersion: Int) {
        launch {
            interviewListState.value =
                repository.getInterviewList(
                    userName = userName,
                    password = password,
                    appVersion = appVersion
                )
        }
    }
}