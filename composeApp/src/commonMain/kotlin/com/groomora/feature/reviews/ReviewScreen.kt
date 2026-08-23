package com.groomora.feature.reviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.groomora.design.Champagne
import com.groomora.design.Charcoal
import com.groomora.design.WarmIvory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    targetId: String,
    type: ReviewTargetType,
    viewModel: ReviewViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(targetId, type) {
        viewModel.onIntent(ReviewIntent.LoadReviews(targetId, type))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reviews & Ratings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Charcoal,
                    titleContentColor = Champagne,
                    navigationIconContentColor = Champagne
                )
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Charcoal)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WarmIvory)
                    .padding(padding)
            ) {
                item {
                    ReviewSummaryHeader(state.averageRating, state.totalReviews)
                }

                items(state.reviews) { review ->
                    ReviewItem(review)
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f), modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}

@Composable
fun ReviewSummaryHeader(rating: Double, total: Int) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Simple string rounding for KMP commonMain
            val displayRating = ((rating * 10).toInt() / 10.0).toString()
            Text(
                text = displayRating,
                style = MaterialTheme.typography.displayMedium,
                color = Charcoal
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(5) { index ->
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = if (index < rating.toInt()) Color(0xFFFFC107) else Color.LightGray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Based on $total reviews",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(review.userName, style = MaterialTheme.typography.titleMedium)
            Text(review.date, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
        
        Row(modifier = Modifier.padding(vertical = 4.dp)) {
            repeat(5) { index ->
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = if (index < review.rating) Color(0xFFFFC107) else Color.LightGray,
                    modifier = Modifier.size(16.dp)
                )
            }
            if (review.isVerified) {
                Spacer(Modifier.width(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.CheckCircle, 
                        contentDescription = null, 
                        modifier = Modifier.size(12.dp), 
                        tint = Color(0xFF2E7D5B)
                    )
                    Text(
                        "Verified Visit", 
                        style = MaterialTheme.typography.labelSmall, 
                        color = Color(0xFF2E7D5B), 
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
            }
        }
        
        Text(review.comment, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 4.dp))
        
        review.reply?.let { reply ->
            Surface(
                modifier = Modifier.padding(top = 12.dp),
                color = Champagne.copy(alpha = 0.1f),
                shape = MaterialTheme.shapes.small
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Response from Shop", style = MaterialTheme.typography.labelLarge, color = Charcoal)
                    Text(reply, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
