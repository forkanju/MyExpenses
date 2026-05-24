package ngo.friendship.mhealth.dc.presentation.screen.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.flow.collectLatest
import ngo.friendship.mhealth.dc.presentation.base.ColoredSnackbarVisuals
import ngo.friendship.mhealth.dc.presentation.base.SnackbarType
import ngo.friendship.mhealth.dc.presentation.components.CompactTextStyle
import ngo.friendship.mhealth.dc.presentation.screen.dashboard.components.DashboardCard
import ngo.friendship.mhealth.dc.theme.FontSize
import ngo.friendship.mhealth.dc.theme.FriendshipTheme
import ngo.friendship.mhealth.dc.theme.PrimaryColor
import ngo.friendship.mhealth.dc.theme.Resources.Icon.Hand
import ngo.friendship.mhealth.dc.theme.TextSecondary
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardScreen(
    onNavigate: (NavKey) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is DashboardEffect.NavigateTo -> onNavigate(effect.route)
                is DashboardEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        ColoredSnackbarVisuals(
                            message = effect.message,
                            type = effect.type
                        )
                    )
                }
            }
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(
                ColoredSnackbarVisuals(
                    message = it,
                    type = SnackbarType.ERROR
                )
            )
            viewModel.onIntent(DashboardIntent.ClearError)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { viewModel.onIntent(DashboardIntent.Refresh) },
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            DashboardContent(
                state = state,
                onIntent = viewModel::onIntent
            )
        }
    }
}

@Composable
fun DashboardContent(
    state: DashboardState,
    onIntent: (DashboardIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
    ) {
        val screenWidth = maxWidth

        // 1. Determine column count based on width
        val columnCount = when {
            screenWidth < 600.dp -> 3 // Phone
            screenWidth < 900.dp -> 4 // Small Tablet
            else -> 6                 // Desktop / Large Tablet
        }

        val spacing = 8.dp
        val padding = 16.dp

        // 2. Precise calculation for adaptive width
        // Formula: (Total Width - Outer Margins - Internal Gaps) / Column Count
        val itemWidth = (screenWidth - (padding * 2) - (spacing * (columnCount - 1))) / columnCount

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(padding),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            state.sections.forEach { section ->
                item {
                    Text(
                        text = section.title,
                        style = CompactTextStyle(
                            fontSize = FontSize.EXTRA_REGULAR,
                            fontWeight = FontWeight.SemiBold,
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // CHUNKING LOGIC: Group cards into rows based on the adaptive columnCount
                    val rows = section.cards.chunked(columnCount)

                    Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
                        rows.forEach { rowCards ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(IntrinsicSize.Max), // Makes all cards in THIS specific row same height
                                horizontalArrangement = Arrangement.spacedBy(spacing)
                            ) {
                                rowCards.forEach { cardData ->
                                    DashboardCard(
                                        data = cardData,
                                        modifier = Modifier.width(itemWidth)
                                    )
                                }

                                // Fill empty space if the last row isn't full
                                repeat(columnCount - rowCards.size) {
                                    Spacer(modifier = Modifier.width(itemWidth))
                                }
                            }
                        }
                    }
                }
            }
            item {
//                Text(
//                    text = "Local treatment",
//                    style = CompactTextStyle(
//                        fontSize = FontSize.EXTRA_REGULAR,
//                        color = TextSecondary,
//                        fontWeight = FontWeight.SemiBold,
//                    )
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//                LocalTreatmentCard(
//                    onClick = { onIntent(DashboardIntent.Navigate(Screens.LocalTreatment)) }
//                )
            }
        }
    }
}

@Composable
fun LocalTreatmentCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Friendship employee 33",
                    style = TextStyle(

                        fontSize = FontSize.EXTRA_REGULAR,
                        color = TextSecondary,
                        fontWeight = FontWeight.Normal
                    )
                )
                Text(
                    text = "Others 16",
                    style = TextStyle(

                        fontSize = FontSize.EXTRA_REGULAR,
                        color = TextSecondary,
                        fontWeight = FontWeight.Normal
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "See All",
                    color = PrimaryColor,
                    style = TextStyle(
                        fontSize = FontSize.SMALL,

                        fontWeight = FontWeight.Normal
                    ),
                    textDecoration = TextDecoration.Underline
                )
            }
            Surface(
                shape = CircleShape,
                color = PrimaryColor,
                modifier = Modifier.size(48.dp),
                shadowElevation = 4.dp
            ) {
                Icon(
                    painter = painterResource(Hand),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(72.dp)
                        .padding(6.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HealthDashboardScreenPreview() {
    FriendshipTheme {
        DashboardContent(
            state = DashboardState(),
            onIntent = {}
        )
    }
}
