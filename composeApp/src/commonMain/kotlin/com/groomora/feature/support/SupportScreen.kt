package com.groomora.feature.support

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.groomora.design.Champagne
import com.groomora.design.Charcoal
import com.groomora.design.WarmIvory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(
    viewModel: SupportViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Help & Support") },
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
        Column(modifier = Modifier.fillMaxSize().background(WarmIvory).padding(padding)) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = Charcoal,
                indicator = { tabPositions ->
                    if (selectedTab < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = Charcoal
                        )
                    }
                }
            ) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                    Text("FAQs", modifier = Modifier.padding(16.dp))
                }
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                    Text("My Tickets", modifier = Modifier.padding(16.dp))
                }
            }

            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Charcoal)
                }
            } else {
                when (selectedTab) {
                    0 -> FaqList(state.faqs)
                    1 -> TicketList(state.tickets)
                }
            }
        }
    }
}

@Composable
fun FaqList(faqs: List<FaqItem>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SupportContactSection()
        }
        
        items(faqs) { faq ->
            FaqAccordionItem(faq)
        }
    }
}

@Composable
fun FaqAccordionItem(faq: FaqItem) {
    var expanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.clickable { expanded = !expanded }.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(faq.question, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
                Icon(
                    imageVector = if (expanded) Icons.Default.Info else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
            if (expanded) {
                Spacer(Modifier.height(8.dp))
                Text(faq.answer, style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)
            }
        }
    }
}

@Composable
fun TicketList(tickets: List<SupportTicket>) {
    Box(Modifier.fillMaxSize()) {
        if (tickets.isEmpty()) {
            Text("No support tickets yet.", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tickets) { ticket ->
                    TicketItem(ticket)
                }
            }
        }
        
        FloatingActionButton(
            onClick = { /* TODO */ },
            modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp),
            containerColor = Charcoal,
            contentColor = Champagne
        ) {
            Icon(Icons.Default.Add, contentDescription = "New Ticket")
        }
    }
}

@Composable
fun TicketItem(ticket: SupportTicket) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Ticket #${ticket.id}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                Surface(
                    color = if (ticket.status == "Resolved") Color(0xFF2E7D5B).copy(alpha = 0.1f) else Color(0xFFC9A227).copy(alpha = 0.1f),
                    shape = MaterialTheme.shapes.extraSmall
                ) {
                    Text(
                        ticket.status,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (ticket.status == "Resolved") Color(0xFF2E7D5B) else Color(0xFFC9A227)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(ticket.subject, style = MaterialTheme.typography.titleMedium)
            ticket.lastMessage?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, color = Color.Gray, maxLines = 1)
            }
            Spacer(Modifier.height(8.dp))
            Text(ticket.date, style = MaterialTheme.typography.labelSmall, color = Color.LightGray)
        }
    }
}

@Composable
fun SupportContactSection() {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text("Contact Us", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            ContactCard("Call Us", Icons.Default.Phone, Charcoal)
            ContactCard("Email Us", Icons.Default.Email, Color(0xFF008080))
        }
    }
}

@Composable
fun ContactCard(label: String, icon: ImageVector, color: Color) {
    Surface(
        modifier = Modifier.width(120.dp),
        shape = MaterialTheme.shapes.medium,
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = color)
            Spacer(Modifier.height(8.dp))
            Text(label, style = MaterialTheme.typography.labelMedium)
        }
    }
}
