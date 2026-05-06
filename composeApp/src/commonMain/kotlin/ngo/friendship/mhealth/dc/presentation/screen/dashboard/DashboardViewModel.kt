package ngo.friendship.mhealth.dc.presentation.screens.dashboard

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ngo.friendship.mhealth.dc.presentation.navigation.Screens
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.model.DashboardCardData
import ngo.friendship.mhealth.dc.presentation.screens.dashboard.model.SectionData
import ngo.friendship.mhealth.dc.theme.PrimaryColor
import ngo.friendship.mhealth.dc.theme.Resources.Icon.Report

class DashboardViewModel : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    private val _effect = Channel<DashboardEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadDashboardData()
    }

    //

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
                        "Templates 32+",
                        Report,
                        PrimaryColor,
                        "Create new",
                        "Import from global",
                        onClick = { onIntent(DashboardIntent.Navigate(Screens.PrescriptionTemplateList)) }
                    ),
                    DashboardCardData(
                        "Add DX",
                        "List 800+",
                        Report,
                        Color(0xFFF0914E),
                        "Create new",
                        onClick = { onIntent(DashboardIntent.Navigate(Screens.DxList)) }
                    ),
                    DashboardCardData(
                        "Medicine List",
                        "Medicine 800+",
                        Report,
                        Color(0xFF4BB652),
                        "Create new",
                        onClick = { onIntent(DashboardIntent.Navigate(Screens.MedicineList)) }
                    ),
                    DashboardCardData(
                        "Advice templates",
                        "Advice 32+",
                        Report,
                        Color(0xFF707070),
                        "Create new",
                        onClick = { onIntent(DashboardIntent.Navigate(Screens.AdviceTemplateList)) }
                    ),
                    DashboardCardData(
                        "Investigations templates",
                        "Templates 32+",
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
