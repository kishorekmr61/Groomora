package com.groomora.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.groomora.design.*
import com.groomora.design.GroomoraImage
import com.groomora.design.components.GroomoraPrimaryButton
import kotlinx.coroutines.launch

data class OnboardingPage(
    val tag: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val accentColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onFinish: () -> Unit
) {
    val pages = listOf(
        OnboardingPage(
            tag = "TOP-RATED SALONS & BARBERS",
            title = "Discover & Book\nLuxury Salons",
            description = "Explore top-rated hair stylists, certified barbers, and premium salons near you with real-time slot booking.",
            imageUrl = "https://images.unsplash.com/photo-1503951914875-452162b0f3f1?w=900&q=80",
            accentColor = Charcoal
        ),
        OnboardingPage(
            tag = "DOORSTEP CONVENIENCE",
            title = "Salon & Spa Care\nat Your Doorstep",
            description = "Certified salon specialists arrive at your home with sealed single-use kits and complete mess-free cleanup.",
            imageUrl = "https://images.unsplash.com/photo-1560066984-138dadb4c035?w=900&q=80",
            accentColor = Color(0xFF008080)
        ),
        OnboardingPage(
            tag = "BRIDAL & CELEBRATION GLOW",
            title = "Exclusive Bridal\n& Event Makeovers",
            description = "Customized pre-bridal rituals, glowing facials, hair couture, and party makeup for your most special moments.",
            imageUrl = "https://images.unsplash.com/photo-1583939003579-730e3918a45a?w=900&q=80",
            accentColor = Color(0xFF800020)
        ),
        OnboardingPage(
            tag = "SHOP & EARN REWARDS",
            title = "Salon Products &\nLoyalty Cashbacks",
            description = "Order 100% genuine haircare and grooming essentials while earning Groomora Coins on every single booking.",
            imageUrl = "https://images.unsplash.com/photo-1608248597359-009eb73d6d03?w=900&q=80",
            accentColor = HoneyAmber
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Brand Name
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Charcoal),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("G", color = HoneyAmber, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "GROOMORA",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        color = Charcoal
                    )
                }

                // Skip Button
                if (pagerState.currentPage < pages.size - 1) {
                    TextButton(onClick = onFinish) {
                        Text(
                            text = "Skip",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MutedText
                        )
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 12.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Animated Page Indicators
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(pages.size) { index ->
                            val isSelected = pagerState.currentPage == index
                            Box(
                                modifier = Modifier
                                    .height(8.dp)
                                    .width(if (isSelected) 26.dp else 8.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) HoneyAmber else Color.LightGray)
                                    .clickable {
                                        scope.launch {
                                            pagerState.animateScrollToPage(index)
                                        }
                                    }
                            )
                        }
                    }

                    // Next / Get Started Button
                    val isLastPage = pagerState.currentPage == pages.size - 1
                    GroomoraPrimaryButton(
                        text = if (isLastPage) "Get Started" else "Continue",
                        onClick = {
                            if (isLastPage) {
                                onFinish()
                            } else {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        },
                        height = 46.dp,
                        modifier = Modifier.width(if (isLastPage) 150.dp else 130.dp)
                    )
                }
            }
        }
    ) { padding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .background(WarmIvory)
                .padding(padding)
        ) { index ->
            val page = pages[index]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                // Dynamic Hero Image Container with Rounded Corners & Tag
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Charcoal),
                    contentAlignment = Alignment.BottomStart
                ) {
                    GroomoraImage(
                        url = page.imageUrl,
                        contentDescription = page.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Gradient overlay at the bottom of the image
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                                    startY = 200f
                                )
                            )
                    )

                    // Category Tag Badge
                    Surface(
                        color = HoneyAmber,
                        shape = CircleShape,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = page.tag,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                            letterSpacing = 1.sp
                        )
                    }
                }

                // Text Content
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = page.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Charcoal,
                        textAlign = TextAlign.Center,
                        lineHeight = 32.sp
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = page.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MutedText,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}
