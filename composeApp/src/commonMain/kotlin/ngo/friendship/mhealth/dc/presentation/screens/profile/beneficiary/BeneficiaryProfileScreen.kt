package ngo.friendship.mhealth.dc.presentation.screens.profile.beneficiary

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.presentation.screens.case.case_list.components.AvatarBadge
import ngo.friendship.mhealth.dc.theme.PrimaryBlue
import ngo.friendship.mhealth.dc.theme.Resources
import ngo.friendship.mhealth.dc.theme.RobotoCondensedFont
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import ngo.friendship.mhealth.dc.domain.model.InterviewDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeneficiaryProfileScreen(
    beneficiaryId: Long,
    beneficiaryName: String = "",
    beneficiaryAge: String = "",
    location: String = "",
    questionnaireName: String = "",
    interviewDetails: InterviewDetails? = null,
    onBack: () -> Unit,
    viewModel: BeneficiaryProfileViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(beneficiaryId) {
        viewModel.onIntent(
            BeneficiaryProfileIntent.LoadProfile(
                beneficiaryId = beneficiaryId,
                beneficiaryName = beneficiaryName,
                beneficiaryAge = beneficiaryAge,
                location = location,
                questionnaireName = questionnaireName
            )
        )
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                BeneficiaryProfileUiEffect.NavigateBack -> onBack()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "$questionnaireName > $beneficiaryId",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF666666)
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onIntent(BeneficiaryProfileIntent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF666666)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            BeneficiaryHeaderCard(
                name = state.beneficiaryName,
                beneficiaryId = state.beneficiaryId.toString(),
                location = state.location,
                age = state.beneficiaryAge,
                lastVisit = interviewDetails?.startTime ?: ""
            )
            Spacer(modifier = Modifier.height(16.dp))
            TabRowSection(
                selectedTab = state.selectedTab,
                onTabSelected = { viewModel.onIntent(BeneficiaryProfileIntent.SelectTab(it)) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            when (state.selectedTab) {
                0 -> ServiceListSection()
                1 -> PersonalInfoSection(state)
            }
        }
    }
}

@Composable
private fun BeneficiaryHeaderCard(
    name: String,
    beneficiaryId: String,
    location: String,
    age: String,
    lastVisit: String
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarBadge(
                modifier = Modifier.size(width = 70.dp, height = 85.dp),
                idText = beneficiaryId,
                photo = {
                    Image(
                        painter = painterResource(Resources.Icon.FCM), // Placeholder
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "$name- $age",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue,
                    fontFamily = RobotoCondensedFont()
                )
                Text(
                    text = location,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    fontFamily = RobotoCondensedFont()
                )
                Text(
                    text = "01918967530 (Self)", // Placeholder
                    fontSize = 13.sp,
                    color = PrimaryBlue,
                    textDecoration = TextDecoration.Underline,
                    fontFamily = RobotoCondensedFont(),
                    modifier = Modifier.clickable { /* Call */ }
                )
                Text(
                    text = "Visited : $lastVisit",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = RobotoCondensedFont()
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircleActionIcon(
                    painter = painterResource(Resources.Icon.Call),
                    onClick = { /* Call */ }
                )
                Spacer(modifier = Modifier.height(8.dp))
                CircleActionIcon(
                    painter = painterResource(Resources.Icon.Wapp),
                    onClick = { /* Wapp */ }
                )
            }
        }
    }
}

@Composable
private fun TabRowSection(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val tabs = listOf("Service list (07)", "Personal info")
        tabs.forEachIndexed { index, title ->
            TabItem(
                title = title,
                isSelected = selectedTab == index,
                modifier = Modifier.weight(1f),
                onClick = { onTabSelected(index) }
            )
        }
    }
}

@Composable
private fun TabItem(title: String, isSelected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) PrimaryBlue else Color.White,
        border = if (isSelected) null else BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f)),
        modifier = modifier
            .height(36.dp)
            .clickable { onClick() }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = title,
                fontSize = 13.sp,
                color = if (isSelected) Color.White else Color.Black,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

@Composable
private fun ServiceListSection() {
    // Mock list of cases based on image
    val cases = listOf(
        CaseItemData("Oral Ulcer", "DX: Oral ulcer (Aphthous ulcer) - localized shallow", "Tab: Ribonson (5mg), Lotion Viola (1%) 5 days...", "15-Dec-22"),
        CaseItemData("Loose Stool", "DX: Oral ulcer (Aphthous ulcer)", "Orsaline, Tab: Imotil (2mg) (1+1+1) 3 days....", "15-Dec-22"),
        CaseItemData("Fever", "DX: Oral ulcer (Aphthous ulcer) - localized shallow", "Paracetamol 500mg (1+1+1) 3 days....", "15-Dec-22")
    )

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
             Row(
                 modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                 horizontalArrangement = Arrangement.SpaceBetween,
                 verticalAlignment = Alignment.CenterVertically
             ) {
                 Text("Recent Cases", fontSize = 14.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                 Row(verticalAlignment = Alignment.CenterVertically) {
                     Text("Last ", fontSize = 14.sp, color = Color.Gray)
                     Surface(
                         shape = RoundedCornerShape(4.dp),
                         border = BorderStroke(1.dp, Color.Gray),
                         modifier = Modifier.padding(start = 4.dp)
                     ) {
                         Row(modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                             Text("20", fontSize = 13.sp)
                             Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.size(16.dp))
                         }
                     }
                 }
             }
        }
        items(cases) { case ->
            CaseItemRow(case)
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
        }
    }
}

data class CaseItemData(val title: String, val dx: String, val prescription: String, val date: String)

@Composable
private fun CaseItemRow(case: CaseItemData) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(width = 65.dp, height = 75.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            ) {
                 Image(
                    painter = painterResource(Resources.Icon.FCM), // Placeholder
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = case.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(text = case.dx, fontSize = 13.sp, color = PrimaryBlue, lineHeight = 16.sp)
                Text(text = case.prescription, fontSize = 13.sp, color = Color.Gray, lineHeight = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Date: ${case.date}", fontSize = 11.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
private fun PersonalInfoSection(state: BeneficiaryProfileUiState) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileInfoRow("Name", state.beneficiaryName)
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            ProfileInfoRow("Beneficiary ID", state.beneficiaryId.toString())
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            ProfileInfoRow("Location", state.location)
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            ProfileInfoRow("Age", state.beneficiaryAge)
        }
    }
}

@Composable
private fun ProfileInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Normal,
            fontFamily = RobotoCondensedFont()
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium,
            fontFamily = RobotoCondensedFont()
        )
    }
}

@Composable
private fun CircleActionIcon(
    painter: Painter,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(32.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painter,
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
