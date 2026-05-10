package ngo.friendship.mhealth.dc.presentation.screen.dashboard

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ngo.friendship.mhealth.dc.domain.model.SetupData
import ngo.friendship.mhealth.dc.domain.repository.CaseRepository
import ngo.friendship.mhealth.dc.domain.repository.MainRepository
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.model.DashboardCardData
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.model.SectionData
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.DashboardEffect
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.DashboardIntent
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.DashboardState
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
        }
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            val adviceList = try {
                mainRepository.getAdviceList()
            } catch (_: Exception) {
                emptyList()
            }

            val medicineList = try {
                caseRepository.getMedicineList("Tab")
            } catch (_: Exception) {
                emptyList()
            }

            val prescriptionTemplates = try {
                mainRepository.getPrescriptionTemplates()
            } catch (_: Exception) {
                emptyList()
            }

            mainRepository.getSetupData().collect { setupData ->
                updateSections(setupData, adviceList.size, medicineList.size, prescriptionTemplates.size)
            }
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    private fun updateSections(setupData: SetupData, adviceCount: Int, medicineCount: Int, prescriptionCount: Int) {
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
                        iconTint = Color.Unspecified
                    ),
                    DashboardCardData(
                        "Doctor center summary",
                        null,
                        Report,
                        Color(0xFFF1F5FD),
                        "Download",
                        isCentered = true,
                        iconTint = Color.Unspecified
                    ),
                    DashboardCardData(
                        "Disease wise service",
                        null,
                        Report,
                        Color(0xFFF1F5FD),
                        "Download",
                        isCentered = true,
                        iconTint = Color.Unspecified
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
        _state.value = _state.value.copy(sections = sections)
    }
}
