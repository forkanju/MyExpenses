package ngo.friendship.mhealth.dc.presentation.screen.profile.beneficiary

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.domain.model.BeneficiaryServiceItem
import ngo.friendship.mhealth.dc.domain.model.InterviewDetails
import ngo.friendship.mhealth.dc.presentation.screens.case.case_list.components.AvatarBadge
import ngo.friendship.mhealth.dc.presentation.screens.profile.beneficiary.BeneficiaryProfileIntent
import ngo.friendship.mhealth.dc.presentation.screens.profile.beneficiary.BeneficiaryProfileUiEffect
import ngo.friendship.mhealth.dc.presentation.screens.profile.beneficiary.BeneficiaryProfileUiState
import ngo.friendship.mhealth.dc.presentation.screens.profile.beneficiary.BeneficiaryProfileViewModel
import ngo.friendship.mhealth.dc.theme.PrimaryBlue
import ngo.friendship.mhealth.dc.theme.Resources
import ngo.friendship.mhealth.dc.theme.RobotoCondensedFont
import ngo.friendship.mhealth.dc.utils.toUiDate
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeneficiaryProfileScreen(
    beneficiaryId: Long,
    beneficiaryCode: String = "",
    beneficiaryName: String = "",
    beneficiaryAge: String = "",
    location: String = "",
    questionnaireName: String = "",
    interviewDetails: InterviewDetails? = null,
    onBack: () -> Unit,
    viewModel: BeneficiaryProfileViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(beneficiaryId) {
        viewModel.onIntent(
            BeneficiaryProfileIntent.LoadProfile(
                beneficiaryId = beneficiaryId,
                beneficiaryCode = beneficiaryCode,
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
                        "Beneficiary Profile",
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
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                BeneficiaryHeaderCard(
                    name = state.beneficiaryName,
                    beneficiaryId = state.beneficiaryCode.takeLast(5),
                    location = state.location,
                    age = state.beneficiaryAge,
                    mobileNo = state.beneficiaryProfile?.mobileNumber ?: "N/A",
                    lastVisit = state.beneficiaryProfile?.serviceList?.firstOrNull()?.interviewTime
                        ?: interviewDetails?.startTime ?: "",
                    onCallClick = { mobile ->
                        val clean = mobile.filter { it.isDigit() }
                        uriHandler.openUri("tel:$clean")
                    },
                    onWhatsAppClick = { mobile ->
                        val clean = mobile.filter { it.isDigit() }
                        uriHandler.openUri("https://api.whatsapp.com/send?phone=$clean")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                TabRowSection(
                    selectedTab = state.selectedTab,
                    serviceCount = state.beneficiaryProfile?.serviceList?.size ?: 0,
                    onTabSelected = { viewModel.onIntent(BeneficiaryProfileIntent.SelectTab(it)) }
                )
                Spacer(modifier = Modifier.height(16.dp))

                when (state.selectedTab) {
                    0 -> PersonalInfoSection(state)
                    1 -> ServiceListSection(state.beneficiaryProfile?.serviceList ?: emptyList())
                }
            }

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
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
    mobileNo: String,
    onCallClick: (String) -> Unit,
    onWhatsAppClick: (String) -> Unit,
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
                    text = mobileNo, // Placeholder
                    fontSize = 13.sp,
                    color = PrimaryBlue,
                    textDecoration = TextDecoration.Underline,
                    fontFamily = RobotoCondensedFont(),
                    modifier = Modifier.clickable { onCallClick(mobileNo) }
                )
                Text(
                    text = "Visited : ${if (lastVisit.isNotBlank()) lastVisit.toUiDate() else "N/A"}",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontFamily = RobotoCondensedFont()
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircleActionIcon(
                    painter = painterResource(Resources.Icon.Call),
                    onClick = { onCallClick(mobileNo) }
                )
                Spacer(modifier = Modifier.height(8.dp))
                CircleActionIcon(
                    painter = painterResource(Resources.Icon.Wapp),
                    onClick = { onWhatsAppClick(mobileNo) }
                )
            }
        }
    }
}

@Composable
private fun TabRowSection(selectedTab: Int, serviceCount: Int, onTabSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val tabs =
            listOf("Personal info", "Service list (${serviceCount.toString().padStart(2, '0')})")
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
private fun TabItem(
    title: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
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
private fun ServiceListSection(services: List<BeneficiaryServiceItem>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(services) { service ->
            CaseItemRow(service)
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
        }
    }
}

@Composable
private fun CaseItemRow(service: BeneficiaryServiceItem) {
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
                Text(
                    text = service.caseName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Status: ${service.status}",
                    fontSize = 13.sp,
                    color = PrimaryBlue,
                    lineHeight = 16.sp
                )
                Text(
                    text = "Referred to: ${service.referredTo}",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    lineHeight = 16.sp
                )
                Text(
                    text = "Opened by: ${service.lastOpenedBy}",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Date: ${service.interviewTime.toUiDate()}",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
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
            ProfileInfoRow("Beneficiary Code", state.beneficiaryCode.takeLast(5))
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            ProfileInfoRow("Location", state.location)
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            ProfileInfoRow("Age", state.beneficiaryAge)

            state.beneficiaryProfile?.let { profile ->
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                ProfileInfoRow("Guardian Name", profile.guardianName)
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                ProfileInfoRow("Mobile", profile.mobileNumber)
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                ProfileInfoRow("Gender", profile.gender)
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                ProfileInfoRow("Marital Status", profile.maritalStatus)
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                ProfileInfoRow("Occupation", profile.occupation)
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                ProfileInfoRow("Religion", profile.religion)
            }
        }
    }
}

@Composable
private fun ProfileInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Normal,
            fontFamily = RobotoCondensedFont()
        )
        Text(
            text = value,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium,
            fontFamily = RobotoCondensedFont(),
            textAlign = TextAlign.End
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
