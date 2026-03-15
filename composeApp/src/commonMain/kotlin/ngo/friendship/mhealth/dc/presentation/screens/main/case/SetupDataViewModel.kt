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

    private val _setupDataState =
        MutableStateFlow<RequestState<SetupData>>(RequestState.Idle)
    val setupDataState: StateFlow<RequestState<SetupData>> = _setupDataState


    fun loadSetupData(
        userName: String,
        password: String
    ) {
        launch {
            _setupDataState.value = RequestState.Loading

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
                _setupDataState.value = RequestState.Success(result)
            }.onFailure { throwable ->
                _setupDataState.value = RequestState.Error(
                    throwable.message ?: "Failed to load setup data"
                )
            }
        }
    }

}
