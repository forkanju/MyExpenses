package ngo.friendship.mhealth.dc.presentation.screens.main.case.dummy

import PrescriptionTemplateSection
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrescriptionScreen(
) {
    Scaffold(
        topBar = {
            PrescriptionTopBar(
                titlePrefix = "Ref by Most Rina- Cox's Bazar ...",
                onBack = { },
                onViewClick = { },
                onCall = { },
                onWhatsApp = { }
            )
        },
        bottomBar = {
        }
    ) { padding ->

        var dx by remember { mutableStateOf<String?>(null) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PatientProfileCard()
            ExpandableInterviewSummary(
                modifier = Modifier
                    .fillMaxWidth(),
                items = listOf(
                    QAItem("Which oral part is infected?", "Angle of the mouth"),
                    QAItem(
                        "Associated problems along with oral pain?",
                        "Pain increases on movement"
                    ),
                    QAItem(
                        "Does it hurt while eating, drinking, or talking?",
                        "it hurts a little while eating and drinking"
                    ),
                ),
                uploadedText = "16.02, 10 Nov 2026",
                selectedTab = 0,
                onTabSelect = {},
                onCopyClick = {},
                onSeeFullClick = {},
                expandedInitially = true
            )

            FormContainerCard {
                PrescriptionHeader()
                Spacer(modifier = Modifier.height(8.dp))
                PrescriptionTemplateSection(
                    chips = listOf(
                        "Oral ulcer prescription by Abir",
                        "Prescription 2",
                        "Ulcer by me",
                        "Follow up"
                    ),
                    onLinkClick = { },
                    onChipClick = { selected -> }
                )
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(modifier = Modifier.height(1.dp))
                Spacer(modifier = Modifier.height(12.dp))
                FormDropdownField(
                    label = "DX",
                    placeholder = "Type",
                    options = listOf("Type 1", "Type 2", "Type 3"),
                    selected = dx,
                    onSelectedChange = { dx = it }
                )
                Spacer(modifier = Modifier.height(12.dp))
                MedicineAddScreen()
            }
        }
    }
}

