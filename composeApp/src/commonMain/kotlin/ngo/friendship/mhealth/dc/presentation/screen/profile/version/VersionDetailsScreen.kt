package ngo.friendship.mhealth.dc.presentation.screen.profile.version

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.domain.model.VersionInfo
import ngo.friendship.mhealth.dc.theme.Resources
import ngo.friendship.mhealth.dc.theme.RobotoCondensedFont
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VersionDetailsScreen(
    version: VersionInfo,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Version Details", fontFamily = RobotoCondensedFont()) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(Resources.Icon.BackArrow),
                            contentDescription = "Back",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Version ${version.versionName}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = RobotoCondensedFont()
            )
            Text(
                text = "Build: ${version.versionCode} | Released on ${version.releaseDate}",
                fontSize = 14.sp,
                color = Color.Gray,
                fontFamily = RobotoCondensedFont()
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            if (version.features.isNotEmpty()) {
                Text(
                    text = "New Features",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = RobotoCondensedFont(),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                version.features.forEach { feature ->
                    Text(
                        text = "• $feature",
                        fontSize = 15.sp,
                        fontFamily = RobotoCondensedFont(),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (version.bugFixes.isNotEmpty()) {
                Text(
                    text = "Bug Fixes",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = RobotoCondensedFont(),
                    color = Color(0xFFE53935) // Red color for bug fixes
                )
                Spacer(modifier = Modifier.height(8.dp))
                version.bugFixes.forEach { bug ->
                    Text(
                        text = "• $bug",
                        fontSize = 15.sp,
                        fontFamily = RobotoCondensedFont(),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
        }
    }
}
