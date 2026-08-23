package com.groomora.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.groomora.design.Champagne
import com.groomora.design.Charcoal
import com.groomora.design.WarmIvory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(WarmIvory)
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("Preferences", style = MaterialTheme.typography.titleMedium)
            }

            item {
                SettingToggleItem(
                    title = "Marketing Notifications",
                    subtitle = "Receive offers and updates",
                    checked = state.preferences.marketingConsent,
                    onCheckedChange = { viewModel.onIntent(SettingsIntent.ToggleMarketing(it)) }
                )
            }

            item {
                SettingToggleItem(
                    title = "Analytics",
                    subtitle = "Help us improve Groomora",
                    checked = state.preferences.analyticsConsent,
                    onCheckedChange = { /* TODO */ }
                )
            }

            item {
                Text("App Info", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
            }

            item {
                Surface(
                    color = Color.White,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Version", style = MaterialTheme.typography.bodyLarge)
                            Text("0.1.0", color = Color.Gray)
                        }
                        Spacer(Modifier.height(8.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Build", style = MaterialTheme.typography.bodyLarge)
                            Text("Debug", color = Color.Gray)
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(24.dp))
                TextButton(
                    onClick = { /* TODO: Account Deletion */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Delete Account", color = Color.Red)
                }
            }
        }
    }
}

@Composable
fun SettingToggleItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        color = Color.White,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyLarge)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(checkedThumbColor = Champagne, checkedTrackColor = Charcoal)
            )
        }
    }
}
