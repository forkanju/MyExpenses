package ngo.friendship.mhealth.dc.presentation.screen.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.presentation.components.CommonTopBar
import ngo.friendship.mhealth.dc.presentation.screens.case.case_list.components.AvatarBadge
import ngo.friendship.mhealth.dc.theme.FriendshipTheme
import ngo.friendship.mhealth.dc.theme.PrimaryBlue
import ngo.friendship.mhealth.dc.theme.Resources
import org.jetbrains.compose.resources.painterResource

@Composable
fun LocalTreatmentDetailsScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CommonTopBar(
                title = "New",
                onBack = onBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Patient Profile Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.Top) {
                        AvatarBadge(
                            modifier = Modifier.size(56.dp),
                            idText = "4811",
                            photo = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color(0xFF60BF77)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "MK",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )
                                }
                            }
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "Nayamot Ullah Ahmed",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray,
                                    fontSize = 15.sp
                                )
                                Text(" (M | 43y)", color = Color.Gray, fontSize = 14.sp)
                                Spacer(Modifier.weight(1f))
                                Text(
                                    "Details",
                                    color = PrimaryBlue,
                                    fontSize = 14.sp,
                                    modifier = Modifier.clickable { })
                            }
                            Text(
                                "01918967530 | Education (HO)",
                                color = Color.LightGray,
                                fontSize = 13.sp
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "Fiver",
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryBlue,
                                    fontSize = 18.sp
                                )
                                Spacer(Modifier.weight(1f))
                                Row {
                                    Icon(
                                        painter = painterResource(Resources.Icon.Call),
                                        contentDescription = null,
                                        tint = PrimaryBlue,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Icon(
                                        painter = painterResource(Resources.Icon.Wapp),
                                        contentDescription = null,
                                        tint = Color(0xFF4CAF50),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Last Interview at 15:02, 10 Nov 2025",
                        fontSize = 11.sp,
                        color = Color.LightGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Interview Section
            Text("Interview", color = PrimaryBlue, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            "2. Positive tests for HIV, Hepatitis B, Syphilis, Rubella (needs management to protect baby).",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    // Placeholder for Image
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(Resources.Icon.Copy),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Prescription Section
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(Resources.Icon.Note),
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Prescription",
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }

            Spacer(Modifier.height(12.dp))

            // DX
            Text(
                "DX",
                color = Color.Gray,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                    .padding(8.dp)
            ) {
                Text(
                    "Oral ulcer (Aphthous ulcer) — localized shallow ulceration on the buccal",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            // Medicine
            Spacer(Modifier.height(12.dp))
            Text(
                "Medicine",
                color = Color.Gray,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Row {
                        Text("1. Cap  ", fontSize = 14.sp, color = Color.Gray)
                        Column {
                            Text(
                                "Amoxicillin 500",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Text("1+0+1 (7 days) - After meal", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }

            // Advice
            Spacer(Modifier.height(12.dp))
            Text(
                "Advice",
                color = Color.Gray,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                    .padding(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "১ প্রতিদিন ২ - ৩ লিটার পানি খাবেন",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            "২ পরিষ্কার পরিচ্ছন্নতা বজায় চলবেন",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }

            // Investigations
            Spacer(Modifier.height(12.dp))
            Text(
                "Investigations",
                color = Color.Gray,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                    .padding(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "X-ray, CBC",
                        modifier = Modifier.weight(1f),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }

            Text(
                text = "Next follow-up: 25 Nov 2026",
                fontSize = 12.sp,
                color = Color.Gray,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Bottom Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { },
                    modifier = Modifier.width(200.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text("Revisit", color = Color.White)
                }
                Spacer(Modifier.width(16.dp))
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White, CircleShape)
                        .border(1.dp, Color.LightGray.copy(alpha = 0.3f), CircleShape)
                ) {
                    Icon(
                        Icons.Default.Share,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LocalTreatmentDetailsScreenPreview() {
    FriendshipTheme {
        LocalTreatmentDetailsScreen(onBack = {})
    }
}
