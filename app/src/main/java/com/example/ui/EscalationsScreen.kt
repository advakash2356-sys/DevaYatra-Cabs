package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AlertRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EscalationsScreen(
    viewModel: DevaYatraViewModel,
    onBookingClick: (Long) -> Unit
) {
    val activeEscalations by viewModel.activeEscalations.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(translate("needs_attention", viewModel), fontWeight = FontWeight.Bold, color = AlertRed) }
            )
        }
    ) { innerPadding ->
        if (activeEscalations.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = androidx.compose.ui.graphics.Color.Gray, modifier = Modifier.size(64.dp))
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = if (viewModel.currentLanguage.value == "hi") "सभी ठीक! कोई जरूरी समस्या नहीं है।" else "All clear! No urgent issues.",
                        color = androidx.compose.ui.graphics.Color.Gray
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = if (viewModel.currentLanguage.value == "hi") "इन बुकिंग्स पर तुरंत ध्यान देने की आवश्यकता है।" else "These bookings require immediate manual intervention.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                items(activeEscalations) { booking ->
                    EscalationCard(booking, onClick = { onBookingClick(booking.id) })
                }
            }
        }
    }
}
