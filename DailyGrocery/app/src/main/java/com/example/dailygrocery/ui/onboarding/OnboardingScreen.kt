package com.example.dailygrocery.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailygrocery.R
import com.example.dailygrocery.ui.theme.DailyGroceryTheme
import com.example.dailygrocery.ui.theme.PrimaryGreen
import com.example.dailygrocery.ui.theme.PrimaryLightGreen
import kotlinx.coroutines.launch

data class OnboardingPageData(
    val imageRes: Int,
    val title: String,
    val description: String
)

val onboardingPages = listOf(
    OnboardingPageData(
        imageRes = R.drawable.onboarding_1,
        title = "Get Discounts\nOn All Products",
        description = "Lorem ipsum dolor sit amet, consetetur\nsadipscing elitr, sed diam nonumy"
    ),
    OnboardingPageData(
        imageRes = R.drawable.onboarding_2,
        title = "Buy Premium\nQuality Fruits",
        description = "Lorem ipsum dolor sit amet, consetetur\nsadipscing elitr, sed diam nonumy"
    ),
    OnboardingPageData(
        imageRes = R.drawable.onboarding_3,
        title = "Buy Quality\nDairy Products",
        description = "Lorem ipsum dolor sit amet, consetetur\nsadipscing elitr, sed diam nonumy"
    ),
    OnboardingPageData(
        imageRes = R.drawable.onboarding_4,
        title = "Fresh Milk &\nDaily Essentials",
        description = "Lorem ipsum dolor sit amet, consetetur\nsadipscing elitr, sed diam nonumy"
    )
)

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                OnboardingPage(data = onboardingPages[page])
            }

            // Navigation and Indicators at the bottom
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Page Indicator
                Row(
                    Modifier
                        .height(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(onboardingPages.size) { iteration ->
                        val color = if (pagerState.currentPage == iteration) PrimaryGreen else Color(0xFFE0E0E0)
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Get started Button
                Button(
                    onClick = {
                        if (pagerState.currentPage < onboardingPages.size - 1) {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            onFinished()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(14.dp),
                            spotColor = PrimaryGreen.copy(alpha = 0.4f)
                        ),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        PrimaryLightGreen,
                                        PrimaryGreen
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Get started",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.2.sp
                            ),
                            color = Color.White
                        )
                    }
                }
                
                Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
            }
        }
    }
}

@Composable
fun OnboardingPage(data: OnboardingPageData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        
        Text(
            text = data.title,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Black,
                fontSize = 32.sp,
                lineHeight = 40.sp,
                letterSpacing = (-0.5).sp
            ),
            textAlign = TextAlign.Center,
            color = Color.Black
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Text(
            text = data.description,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 17.sp,
                lineHeight = 26.sp,
                color = Color(0xFF9E9E9E),
                letterSpacing = 0.1.sp
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        
        Spacer(modifier = Modifier.height(40.dp))
        
        Image(
            painter = painterResource(id = data.imageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentScale = ContentScale.Fit
        )
        
        // Spacer for the bottom navigation area
        Spacer(modifier = Modifier.height(160.dp))
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun OnboardingPreview() {
    DailyGroceryTheme {
        OnboardingScreen(onFinished = {})
    }
}
