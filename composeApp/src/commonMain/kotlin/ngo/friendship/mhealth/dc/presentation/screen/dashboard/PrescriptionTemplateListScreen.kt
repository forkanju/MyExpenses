package ngo.friendship.mhealth.dc.presentation.screen.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ngo.friendship.mhealth.dc.domain.model.PrescriptionTemplate
import androidx.compose.foundation.clickable
import androidx.compose.runtime.LaunchedEffect
import ngo.friendship.mhealth.dc.presentation.components.CommonTopBar
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.PrescriptionTemplateListIntent
import ngo.friendship.mhealth.dc.theme.PrimaryBlue
import ngo.friendship.mhealth.dc.utils.toUiDate
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PrescriptionTemplateListScreen(
    onBack: () -> Unit,
    onAddTemplate: () -> Unit,
    onTemplateClick: (PrescriptionTemplate) -> Unit,
    viewModel: PrescriptionTemplateListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    
    // Trigger load templates if not loaded
    LaunchedEffect(Unit) {
        viewModel.onIntent(PrescriptionTemplateListIntent.LoadTemplates)
    }

    Scaffold(
        topBar = {
            CommonTopBar(
                title = "Prescription Templates",
                onBack = onBack,
                showSearch = false,
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTemplate,
                containerColor = PrimaryBlue,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 24.dp, end = 24.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { viewModel.onIntent(PrescriptionTemplateListIntent.Refresh) },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (state.templates.isEmpty()) {
                    Text(
                        text = "No templates found",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Gray
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.templates) { template ->
                            TemplateItem(
                                template = template,
                                onClick = { onTemplateClick(template) },
                                onEdit = { onTemplateClick(template) },
                                onDelete = { viewModel.onIntent(PrescriptionTemplateListIntent.DeleteTemplate(template)) }
                            )
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TemplateItem(
    template: PrescriptionTemplate,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Initial Icon
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(template.color)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = template.title.take(2).uppercase(),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            val displayTitle = template.title.substringBefore(" Prescription").trim()
            Text(
                text = displayTitle,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
            val subtitle = buildString {
                if (template.updatedDate.isNotBlank()) {
                    append("Updated: at ${template.updatedDate.toUiDate()} ")
                }
                append("Created: at ${template.createdDate.toUiDate()}")
            }
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = Color.Gray
            )
        }

        Box {
            IconButton(onClick = { showMenu = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More",
                    tint = Color.Gray
                )
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Edit") },
                    onClick = {
                        showMenu = false
                        onEdit()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = {
                        showMenu = false
                        onDelete()
                    }
                )
            }
        }
    }
}
