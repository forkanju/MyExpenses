package ngo.friendship.mhealth.dc.presentation.screen.profile.version

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.domain.model.VersionInfo
import ngo.friendship.mhealth.dc.theme.Resources
import ngo.friendship.mhealth.dc.theme.RobotoCondensedFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VersionHistoryScreen(
    onVersionClick: (VersionInfo) -> Unit,
    onBack: () -> Unit
) {
    val versions = listOf(
        VersionInfo(
            versionName = "1.0.5",
            versionCode = 3069,
            releaseDate = "24 June 2026",
            features = listOf("Dynamic server status indicator", "Data sync from dashboard", "Mandatory prescription validation"),
            bugFixes = listOf("Profile image clipping fix", "Medicine instruction field mapping fix")
        ),
        VersionInfo(
            versionName = "1.0.4",
            versionCode = 3068,
            releaseDate = "15 June 2026",
            features = listOf("Prescription template support", "Auto-complete for generic names"),
            bugFixes = listOf("Login session timeout issue")
        ),
        VersionInfo(
            versionName = "1.0.0",
            versionCode = 3000,
            releaseDate = "01 Jan 2026",
            features = listOf("Initial release of Doctor Center app"),
            bugFixes = emptyList()
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Version History", fontFamily = RobotoCondensedFont()) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = org.jetbrains.compose.resources.painterResource(Resources.Icon.BackArrow),
                            contentDescription = "Back",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(versions) { version ->
                VersionRow(
                    version = version,
                    onClick = { onVersionClick(version) }
                )
                HorizontalDivider(color = Color(0xFFEEEEEE))
            }
        }
    }
}

@Composable
fun VersionRow(
    version: VersionInfo,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Version: ${version.versionName}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = RobotoCondensedFont()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Release Date: ${version.releaseDate}",
                fontSize = 14.sp,
                color = Color.Gray,
                fontFamily = RobotoCondensedFont()
            )
        }
        Icon(
            painter = org.jetbrains.compose.resources.painterResource(Resources.Icon.RightArrow),
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}
