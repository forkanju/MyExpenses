package ngo.friendship.mhealth.dc.presentation.screen.profile.fcm

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import ngo.friendship.mhealth.dc.domain.model.FcmProfile
import ngo.friendship.mhealth.dc.presentation.screens.case.case_list.components.AvatarBadge
import ngo.friendship.mhealth.dc.presentation.screens.profile.fcm.FcmProfileViewModel
import ngo.friendship.mhealth.dc.theme.PrimaryBlue
import ngo.friendship.mhealth.dc.theme.Resources
import ngo.friendship.mhealth.dc.theme.RobotoCondensedFont
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FcmProfileScreen(
    fcmCode: String,
    fcmProfile: FcmProfile? = null,
    onBack: () -> Unit,
    viewModel: FcmProfileViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(fcmProfile) {
        if (fcmProfile != null) {
            viewModel.onIntent(FcmProfileIntent.SetFcmProfile(fcmProfile))
        }
    }

    LaunchedEffect(fcmCode) {
        viewModel.onIntent(FcmProfileIntent.LoadProfile(fcmCode))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                FcmProfileUiEffect.NavigateBack -> onBack()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "FCM Profile", style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Medium, color = Color(0xFF666666)
                        )
                    )
                }, navigationIcon = {
                    IconButton(onClick = { viewModel.onIntent(FcmProfileIntent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF666666)
                        )
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }, containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            ProfileHeaderCard(state = state, onCallClick = { mobile ->
                val clean = mobile.filter { it.isDigit() }
                if (clean.isNotEmpty()) {
                    uriHandler.openUri("tel:$clean")
                }
            }, onWhatsAppClick = { mobile ->
                val clean = mobile.filter { it.isDigit() }
                if (clean.isNotEmpty()) {
                    uriHandler.openUri("https://api.whatsapp.com/send?phone=$clean")
                }
            })
            Spacer(modifier = Modifier.height(16.dp))
            TabRowSection(
                selectedTab = state.selectedTab,
                onTabSelected = { viewModel.onIntent(FcmProfileIntent.SelectTab(it)) })
            Spacer(modifier = Modifier.height(16.dp))

            if (state.selectedTab == 0) {
                ProfileInfoSection(state.fcmProfile)
            }
        }
    }
}

@Composable
private fun ProfileHeaderCard(
    state: FcmProfileUiState, onCallClick: (String) -> Unit, onWhatsAppClick: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarBadge(
                modifier = Modifier.size(80.dp), idText = state.fcmProfile?.loginId ?: "", photo = {
                    Image(
                        painter = painterResource(Resources.Icon.FCM),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                })
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = state.fcmProfile?.userName ?: "",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue,
                    fontFamily = RobotoCondensedFont()
                )
                Text(
                    text = state.fcmProfile?.location ?: "",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = RobotoCondensedFont()
                )
                Text(
                    text = "${state.fcmProfile?.mobileNo ?: ""} (Self)",
                    fontSize = 14.sp,
                    color = PrimaryBlue,
                    textDecoration = TextDecoration.Underline,
                    fontFamily = RobotoCondensedFont(),
                    modifier = Modifier.clickable {
                        state.fcmProfile?.mobileNo?.let { onCallClick(it) }
                    })
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircleActionIcon(
                    painter = painterResource(Resources.Icon.Call), onClick = {
                        state.fcmProfile?.mobileNo?.let { onCallClick(it) }
                    })
                Spacer(modifier = Modifier.width(8.dp))
                CircleActionIcon(
                    painter = painterResource(Resources.Icon.Wapp), onClick = {
                        state.fcmProfile?.mobileNo?.let { onWhatsAppClick(it) }
                    })
            }
        }
    }
}

@Composable
private fun TabRowSection(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val tabs = listOf("Personal info")
        tabs.forEachIndexed { index, title ->
            TabItem(
                title = title,
                isSelected = selectedTab == index,
                modifier = Modifier.weight(1f),
                onClick = { onTabSelected(index) })
        }
    }
}

@Composable
private fun TabItem(
    title: String, isSelected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) PrimaryBlue else Color.White,
        border = if (isSelected) null else BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f)),
        modifier = modifier.height(36.dp).clickable { onClick() }) {
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
private fun ProfileInfoSection(fcmProfile: FcmProfile?) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileInfoRow("Name", fcmProfile?.userName ?: "")
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

            ProfileInfoRow("Login ID", fcmProfile?.loginId ?: "")
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            ProfileInfoRow("Location", fcmProfile?.location ?: "")
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            ProfileInfoRow("Mobile", fcmProfile?.mobileNo ?: "")
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            ProfileInfoRow("Organization", fcmProfile?.orgName ?: "")
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
            modifier = Modifier.weight(.5f),
            fontSize = 14.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Normal,
            fontFamily = RobotoCondensedFont()
        )
        Text(
            text = value,
            modifier = Modifier.weight(.5f),
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
    painter: Painter, onClick: () -> Unit
) {
    IconButton(
        onClick = onClick, modifier = Modifier.size(32.dp)
    ) {
        Box(
            modifier = Modifier.size(28.dp).clip(CircleShape), contentAlignment = Alignment.Center
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
