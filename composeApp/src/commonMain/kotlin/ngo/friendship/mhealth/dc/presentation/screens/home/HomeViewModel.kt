package ngo.friendship.mhealth.dc.presentation.screens.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ngo.friendship.mhealth.dc.data.local.LocalSettings
import ngo.friendship.mhealth.dc.data.remote.ApiService
import ngo.friendship.mhealth.dc.data.remote.dto.DashboardDataReqDto
import ngo.friendship.mhealth.dc.data.remote.dto.DashboardDataResDto

class HomeViewModel(
    private val api: ApiService,
    private val localSettings: LocalSettings
) : ViewModel() {

    private val _dashboardState = mutableStateOf<DashboardDataResDto?>(null)
    val dashboardState: State<DashboardDataResDto?> = _dashboardState

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    init {
        getDashboardData()
    }

    fun getDashboardData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = api.getDashboardData(
                    DashboardDataReqDto.build(
                        userName = localSettings.user.userName,
                        password = localSettings.user.password
                    )
                )
                _dashboardState.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}