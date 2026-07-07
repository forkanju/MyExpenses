package com.example.myexpenses.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import android.content.Intent
import com.example.myexpenses.MainViewModel
import com.example.myexpenses.data.Expense
import com.example.myexpenses.ui.components.ExpenseBottomSheet
import com.example.myexpenses.ui.components.ExpenseItem
import com.example.myexpenses.utils.CurrencyUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.FloatingActionButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: MainViewModel) {
    val expenses by viewModel.expenses.collectAsStateWithLifecycle()
    val totalSpent by viewModel.totalSpent.collectAsStateWithLifecycle()
    val selectedMonth by viewModel.selectedMonth.collectAsStateWithLifecycle()
    val availableMonths = viewModel.availableMonths
    val context = LocalContext.current

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = { Text("MyExpenses", fontWeight = FontWeight.Bold) },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            TotalSpentCard(totalSpent = totalSpent)

            // Month Selection Filter
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(availableMonths) { month ->
                    FilterChip(
                        selected = selectedMonth == month,
                        onClick = { viewModel.selectMonth(month) },
                        label = { Text(month.getName()) }
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedMonth.getName(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = {
                    viewModel.exportToCsv(
                        onSuccess = { file ->
                            val uri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.fileprovider",
                                file
                            )
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/csv"
                                putExtra(Intent.EXTRA_STREAM, uri)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(Intent.createChooser(intent, "Download Expenses"))
                        },
                        onError = { /* Handle error, e.g., show Toast */ }
                    )
                }) {
                    Icon(Icons.Default.Download, contentDescription = "Download CSV")
                }
            }

            if (expenses.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No expenses for ${selectedMonth.getName()}.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    items(
                        items = expenses, key = { it.id }) { expense ->
                        SwipeToRevealDelete(
                            onDelete = { viewModel.deleteExpense(expense) }) {
                            ExpenseItem(
                                expense = expense, modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.openEditExpense(expense)
                                    })
                        }
                    }
                }
            }
        }
    }

    if (viewModel.showBottomSheet) {
        val categories by viewModel.categories.collectAsStateWithLifecycle()
        val paymentModes by viewModel.paymentModes.collectAsStateWithLifecycle()
        val suggestions by viewModel.descriptionSuggestions.collectAsStateWithLifecycle()
        ExpenseBottomSheet(
            expense = viewModel.editingExpense,
            availableCategories = categories.map { it.name },
            availablePaymentModes = paymentModes.map { it.name },
            descriptionSuggestions = suggestions,
            onAddCategory = { viewModel.addCategory(it) },
            onAddPaymentMode = { viewModel.addPaymentMode(it) },
            onSave = {
                viewModel.addExpense(it)
                viewModel.closeBottomSheet()
            }, onDismiss = {
                viewModel.closeBottomSheet()
            })
    }
}

@Composable
fun SwipeToRevealDelete(
    onDelete: () -> Unit, content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val maxOffset = with(density) { -80.dp.toPx() }
    val offset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    var autoCloseJob by remember { mutableStateOf<Job?>(null) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .height(IntrinsicSize.Min)
    ) {
        // Background Delete Button
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .width(96.dp) // 80dp visible + 16dp overlap
                .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                .background(Color.Red)
                .clickable {
                    scope.launch {
                        autoCloseJob?.cancel()
                        offset.animateTo(0f)
                        onDelete()
                    }
                }, contentAlignment = Alignment.Center
        ) {
            // Center the icon in the visible 80dp area
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(80.dp)
                    .align(Alignment.CenterEnd),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Delete, contentDescription = "Delete", tint = Color.White
                )
            }
        }

        // Foreground Content
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offset.value.roundToInt(), 0) }
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(onDragStart = {
                        autoCloseJob?.cancel()
                    }, onDragEnd = {
                        scope.launch {
                            if (offset.value < maxOffset / 2) {
                                offset.animateTo(maxOffset)
                                // Start 5-second timer to auto-close
                                autoCloseJob = launch {
                                    delay(5000.milliseconds)
                                    offset.animateTo(0f)
                                }
                            } else {
                                offset.animateTo(0f)
                            }
                        }
                    }, onHorizontalDrag = { _, dragAmount ->
                        scope.launch {
                            val newOffset = (offset.value + dragAmount).coerceIn(maxOffset, 0f)
                            offset.snapTo(newOffset)
                        }
                    })
                }) {
            content()
        }
    }
}

@Composable
fun TotalSpentCard(totalSpent: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ), elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Total Spent",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = CurrencyUtils.formatBDT(totalSpent),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
