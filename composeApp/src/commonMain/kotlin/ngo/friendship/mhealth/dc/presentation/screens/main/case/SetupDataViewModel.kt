package ngo.friendship.mhealth.dc.presentation.screens.main.case

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ngo.friendship.mhealth.dc.domain.model.SetupData
import ngo.friendship.mhealth.dc.domain.repository.SetupDataRepository
import ngo.friendship.mhealth.dc.presentation.base.BaseViewModel
import ngo.friendship.mhealth.dc.presentation.state.RequestState

class SetupDataViewModel(
    private val repository: SetupDataRepository
) : BaseViewModel() {

    val setupDataState : StateFlow<RequestState<SetupData>>
        field = MutableStateFlow<RequestState<SetupData>>(RequestState.Idle)


    fun loadSetupData(
        userName: String,
        password: String
    ) {
        launch {
            setupDataState.value = RequestState.Loading

            runCatching {

                val cached = repository.getCachedSetupData()

                if (
                    cached.diagnoses.isNotEmpty() ||
                    cached.investigations.isNotEmpty() ||
                    cached.referralCenters.isNotEmpty() ||
                    cached.medicineBrandTypes.isNotEmpty()
                ) {
                    cached
                } else {
                    repository.getSetupData(
                        userName = userName,
                        password = password
                    )
                }

            }.onSuccess { result ->
                setupDataState.value = RequestState.Success(result)
            }.onFailure { throwable ->
                setupDataState.value = RequestState.Error(
                    throwable.message ?: "Failed to load setup data"
                )
            }
        }
    }

}
