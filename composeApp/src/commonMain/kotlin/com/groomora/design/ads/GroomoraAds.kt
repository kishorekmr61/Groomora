package com.groomora.design.ads

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.groomora.core.ads.AdUnitIds
import com.groomora.design.*
import com.groomora.design.components.*

/**
 * Standard Inline Banner Ad Composable.
 */
@Composable
fun GroomoraAdBanner(
    modifier: Modifier = Modifier,
    adUnitId: String = AdUnitIds.Android.getBannerId(),
    onAdClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onAdClick),
        shape = RoundedCornerShape(12.dp),
        color = WarmIvory,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Sponsored "AD" Badge
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Charcoal.copy(alpha = 0.85f)
                ) {
                    Text(
                        text = "AD",
                        color = Champagne,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }

                Column {
                    Text(
                        text = "Sponsored Grooming Deals",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = AppText
                    )
                    Text(
                        text = "Top beauty & wellness partners near you",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedText,
                        fontSize = 11.sp
                    )
                }
            }

            Surface(
                shape = CircleShape,
                color = HoneyAmber.copy(alpha = 0.15f)
            ) {
                Text(
                    text = "Visit",
                    color = HoneyAmber,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    }
}

/**
 * Native Ad Card styled seamlessly for lists and feed views.
 */
@Composable
fun GroomoraNativeAdCard(
    advertiser: String = "L'Oréal Professionnel",
    title: String = "Discover Salon-Grade Hair Nourishment",
    description: String = "Get up to 25% off styling serum when booked with your next haircut.",
    ctaText: String = "Explore Deals",
    imageUrl: String = "https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?w=500&auto=format&fit=crop&q=60",
    onCtaClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    GroomoraCard(
        modifier = modifier.fillMaxWidth(),
        elevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = HoneyAmber
                    ) {
                        Text(
                            text = "SPONSORED",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = advertiser,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MutedText
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            GroomoraImage(
                url = imageUrl,
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(8.dp))
            )


            Spacer(Modifier.height(10.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = AppText
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MutedText
            )

            Spacer(Modifier.height(10.dp))

            GroomoraSecondaryButton(
                text = ctaText,
                onClick = onCtaClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Interactive Rewarded Ad Card for earning bonus loyalty rewards.
 */
@Composable
fun GroomoraRewardedAdCard(
    rewardPoints: Int = 50,
    onWatchAd: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Charcoal,
        border = androidx.compose.foundation.BorderStroke(1.dp, HoneyAmber.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(HoneyAmber.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Watch Video Ad",
                        tint = Champagne,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column {
                    Text(
                        text = "Earn +$rewardPoints Groomora Coins",
                        color = Champagne,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Watch a quick 15s sponsor video",
                        color = Color.White.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.sp
                    )
                }
            }

            Button(
                onClick = onWatchAd,
                colors = ButtonDefaults.buttonColors(
                    containerColor = HoneyAmber,
                    contentColor = Color.White
                ),
                shape = CircleShape,
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Watch Ad",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
