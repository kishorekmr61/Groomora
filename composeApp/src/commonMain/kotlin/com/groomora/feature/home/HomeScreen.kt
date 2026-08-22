package com.groomora.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.groomora.design.Charcoal
import com.groomora.design.Champagne
import com.groomora.design.WarmIvory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GROOMORA") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Charcoal,
                    titleContentColor = Champagne
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .background(WarmIvory)
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Your Style. Your Way.", style = MaterialTheme.typography.headlineMedium)
            Text("Groomora project setup is running successfully.")
            Card {
                Column(Modifier.padding(16.dp)) {
                    Text("Next step", style = MaterialTheme.typography.titleMedium)
                    Text("Continue with the Gemini step-by-step build guide.")
                }
            }
            Button(onClick = {}) { Text("Project Ready") }
        }
    }
}
