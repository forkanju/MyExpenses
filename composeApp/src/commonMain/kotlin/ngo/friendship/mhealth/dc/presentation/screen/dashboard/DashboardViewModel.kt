package ngo.friendship.mhealth.dc.presentation.screen.dashboard

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update
import ngo.friendship.mhealth.dc.domain.model.SetupData
import ngo.friendship.mhealth.dc.domain.repository.CaseRepository
import ngo.friendship.mhealth.dc.domain.repository.MainRepository
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.model.DashboardCardData
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.model.SectionData
import ngo.friendship.mhealth.dc.theme.PrimaryColor
import ngo.friendship.mhealth.dc.theme.Resources.Icon.Report

class DashboardViewModel(
    private val mainRepository: MainRepository,
    private val caseRepository: CaseRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    private val _effect = Channel<DashboardEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadDashboardData()
    }

    fun onIntent(intent: DashboardIntent) {
        when (intent) {
            is DashboardIntent.Navigate -> {
                viewModelScope.launch {
                    _effect.send(DashboardEffect.NavigateTo(intent.route))
                }
            }

            DashboardIntent.LoadDashboard -> loadDashboardData()
            DashboardIntent.ClearError -> _state.update { it.copy(error = null) }
            is DashboardIntent.Refresh -> loadDashboardData(isRefreshing = true)
            is DashboardIntent.ShowSnackbar -> {
                viewModelScope.launch {
                    _effect.send(DashboardEffect.ShowSnackbar(intent.message, type = intent.type))
                }
            }
        }
    }

    private fun loadDashboardData(isRefreshing: Boolean = false) {
        viewModelScope.launch {
            if (isRefreshing) {
                _state.update { it.copy(isRefreshing = true) }
            } else {
                _state.update { it.copy(isLoading = true) }
            }

            try {
                val adviceList = try {
                    mainRepository.getAdviceList()
                } catch (_: Exception) {
                    emptyList()
                }

                val medicineList = try {
                    caseRepository.getMedicineList("Tab", forceRefresh = false)
                } catch (_: Exception) {
                    emptyList()
                }

                val prescriptionTemplates = try {
                    mainRepository.getPrescriptionTemplates()
                } catch (_: Exception) {
                    emptyList()
                }

                mainRepository.getSetupData(forceRefresh = isRefreshing).collect { setupData ->
                    updateSections(
                        setupData,
                        adviceList.size,
                        medicineList.size,
                        prescriptionTemplates.size
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "An error occurred") }
            } finally {
                _state.update { it.copy(isLoading = false, isRefreshing = false) }
            }
        }
    }

    private fun updateSections(
        setupData: SetupData,
        adviceCount: Int,
        medicineCount: Int,
        prescriptionCount: Int
    ) {
        val sections = listOf(
            SectionData(
                title = "Urgent reports",
                cards = listOf(
                    DashboardCardData(
                        "Doctor service engagement...",
                        null,
                        Report,
                        Color(0xFFF1F5FD),
                        "Download",
                        isCentered = true,
                        iconTint = Color.Unspecified,
                        onClick = { onIntent(DashboardIntent.ShowSnackbar("No service available")) }
                    ),
                    DashboardCardData(
                        "Doctor center summary",
                        null,
                        Report,
                        Color(0xFFF1F5FD),
                        "Download",
                        isCentered = true,
                        iconTint = Color.Unspecified,
                        onClick = { onIntent(DashboardIntent.ShowSnackbar("No service available")) }
                    ),
                    DashboardCardData(
                        "Disease wise service",
                        null,
                        Report,
                        Color(0xFFF1F5FD),
                        "Download",
                        isCentered = true,
                        iconTint = Color.Unspecified,
                        onClick = { onIntent(DashboardIntent.ShowSnackbar("No service available")) }
                    )
                )
            ),
            SectionData(
                title = "Resource lab",
                cards = listOf(
                    DashboardCardData(
                        "Prescription templates",
                        "Templates ${prescriptionCount}+",
                        Report,
                        PrimaryColor,
                        "Create new",
                        onClick = { onIntent(DashboardIntent.Navigate(Screens.PrescriptionTemplateList)) }
                    ),
                    DashboardCardData(
                        "Add DX",
                        "List ${setupData.diagnoses.size}+",
                        Report,
                        Color(0xFFF0914E),
                        "Create new",
                        onClick = { onIntent(DashboardIntent.Navigate(Screens.DxList)) }
                    ),
                    DashboardCardData(
                        "Medicine List",
                        "Medicine ${medicineCount}+",
                        Report,
                        Color(0xFF4BB652),
                        "Create new",
                        onClick = { onIntent(DashboardIntent.Navigate(Screens.MedicineList)) }
                    ),
                    DashboardCardData(
                        "Advice templates",
                        "Advice ${adviceCount}+",
                        Report,
                        Color(0xFF707070),
                        "Create new",
                        onClick = { onIntent(DashboardIntent.Navigate(Screens.AdviceTemplateList)) }
                    ),
                    DashboardCardData(
                        "Investigations templates",
                        "Templates ${setupData.investigations.size}+",
                        Report,
                        Color(0xFF707070),
                        "Create new",
                        onClick = { onIntent(DashboardIntent.Navigate(Screens.InvestigationsList)) }
                    )
                )
            )
        )
        _state.update { it.copy(sections = sections) }
    }
}
