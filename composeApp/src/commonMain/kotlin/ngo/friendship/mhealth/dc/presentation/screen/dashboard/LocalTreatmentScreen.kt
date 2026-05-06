package ngo.friendship.mhealth.dc.presentation.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.presentation.screens.case.case_list.components.AvatarBadge
import ngo.friendship.mhealth.dc.presentation.components.CommonTopBar
import ngo.friendship.mhealth.dc.theme.*

@Composable
fun LocalTreatmentScreen(
    onBack: () -> Unit,
    onNavigateToDetails: () -> Unit,
    onAddClick: () -> Unit
) {
    val items = listOf(
        LocalTreatmentItemData("Nayamot Ullah Ahmed", "M | 43y", "01918967530 | Education (HO)", "Fiver", "15:02, 10 Nov 2025", "MK", "4811", Color(0xFF60BF77)),
        LocalTreatmentItemData("Arbindo De Silva", "M | 43y", "01918967530 | ISM (HO)", "Ulcer", "15:02, 10 Nov 2025", "MK", "4811", Color(0xFF214695)),
        LocalTreatmentItemData("Rahim Driver", "M | 43y", "01918967530 | Admin (HO)", "Fiver", "15:02, 10 Nov 2025", "MK", "Others", Color(0xFFD09B7A)),
        LocalTreatmentItemData("Minara Akter", "F | 43y", "01918967530 | Education (HO)", "Cold", "15:02, 10 Nov 2025", "MK", "4811", Color(0xFF60BF77))
    )

    Scaffold(
        topBar = {
            CommonTopBar(
                title = "Local treatment",
                onBack = onBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = PrimaryBlue,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 40.dp, end = 16.dp)
            ) {
                // Custom icon based on screenshot
                Icon(
                    imageVector = Icons.Default.Add, // Placeholder, usually a custom SVG for that icon
                    contentDescription = "Add"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            // Header Filters
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Viewing last", fontSize = 14.sp, color = Color.Gray)
                    Text(" 12 ", fontSize = 14.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                    Text(" Days interviews", fontSize = 14.sp, color = Color.Gray)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Case Loaded: ", fontSize = 14.sp, color = Color.Gray)
                    Text("200 ", fontSize = 14.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                }
            }

            // Search Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .background(Color.White, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Search", color = Color.LightGray, fontSize = 14.sp)
                    }
                }
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.FilterList, contentDescription = null, tint = Color.Gray)
            }

            Spacer(Modifier.height(8.dp))

            // Card Container
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    items(items) { item ->
                        LocalTreatmentItem(
                            item = item,
                            onDetailsClick = onNavigateToDetails
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = Color.LightGray.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

data class LocalTreatmentItemData(
    val name: String,
    val genderAge: String,
    val subInfo: String,
    val diagnosis: String,
    val time: String,
    val initials: String,
    val idCode: String,
    val avatarColor: Color
)

@Composable
fun LocalTreatmentItem(
    item: LocalTreatmentItemData,
    onDetailsClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().clickable { onDetailsClick() }) {
        Row(verticalAlignment = Alignment.Top) {
            // Avatar with Badge
            AvatarBadge(
                modifier = Modifier.size(56.dp),
                idText = item.idCode,
                photo = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(item.avatarColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.initials,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    Text(
                        text = " (${item.genderAge})",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "Details",
                        fontSize = 14.sp,
                        color = PrimaryBlue,
                        modifier = Modifier.clickable { onDetailsClick() }
                    )
                }
                Text(
                    text = item.subInfo,
                    fontSize = 13.sp,
                    color = Color.LightGray
                )
                Text(
                    text = item.diagnosis,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )
            }
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = "Last Interview at ${item.time}",
            fontSize = 11.sp,
            color = Color.LightGray,
            modifier = Modifier.padding(start = 68.dp) // Align with text
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LocalTreatmentScreenPreview() {
    FriendshipTheme {
        LocalTreatmentScreen(onBack = {}, onNavigateToDetails = {}, onAddClick = {})
    }
}
