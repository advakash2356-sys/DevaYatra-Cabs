package com.example.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.Driver
import com.example.data.DriverStatus
import com.example.ui.theme.AlertRed
import com.example.ui.theme.Saffron
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriversScreen(viewModel: DevaYatraViewModel) {
    val drivers by viewModel.allDrivers.collectAsState()
    val context = LocalContext.current
    
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedDriverForAction by remember { mutableStateOf<Driver?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(translate("driver_network", viewModel), fontWeight = FontWeight.Bold) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Saffron,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = translate("add_driver_title", viewModel))
            }
        }
    ) { innerPadding ->
        var searchQuery by remember { mutableStateOf("") }
        val filteredDrivers = remember(drivers, searchQuery) {
            if (searchQuery.isBlank()) {
                drivers
            } else {
                val query = searchQuery.trim().lowercase()
                drivers.filter {
                    it.name.lowercase().contains(query) ||
                    it.phone.lowercase().contains(query) ||
                    it.vehicleNumber.lowercase().contains(query) ||
                    it.vehicleType.lowercase().contains(query)
                }
            }
        }

        Column(modifier = Modifier.padding(innerPadding)) {
            // High-Contrast Search Bar with Voice
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text(if (viewModel.currentLanguage.value == "hi") "खोजने के लिए टाइप करें..." else "Search drivers by name or phone...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    Row {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                        IconButton(onClick = { /* TODO: Voice Search */ }) {
                            Icon(Icons.Default.Mic, contentDescription = "Voice Search", tint = Saffron)
                        }
                    }
                },
                singleLine = true
            )

            if (filteredDrivers.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(if (searchQuery.isNotBlank()) "No matching drivers found" else translate("no_drivers", viewModel))
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (filteredDrivers.isEmpty()) { item { EmptyStateView(title = if (viewModel.currentLanguage.value == "hi") "कोई ड्राइवर नहीं" else "No drivers found", message = if (viewModel.currentLanguage.value == "hi") "यहाँ कोई ड्राइवर नहीं मिला" else "No drivers match your current filters") } } else { items(filteredDrivers) { driver ->
                        DriverCard(driver, onClick = { selectedDriverForAction = driver }, viewModel = viewModel)
                    }
                    }
                    }
                }
            }
        }

        // Dialog to Add a Driver
        if (showAddDialog) {
            var name by remember { mutableStateOf("") }
            var phone by remember { mutableStateOf("") }
            var vehicleNum by remember { mutableStateOf("") }
            var vehicleType by remember { mutableStateOf("Sedan") }
            val types = listOf("Sedan", "SUV", "Hatchback", "Innova", "Auto")

            var hasAttemptedSubmit by remember { mutableStateOf(false) }

            val isNameError = hasAttemptedSubmit && name.isBlank()
            val isPhoneError = hasAttemptedSubmit && (phone.isBlank() || phone.length < 10)
            val isVehicleNumError = hasAttemptedSubmit && vehicleNum.isBlank()

            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text(translate("add_driver_title", viewModel), fontWeight = FontWeight.Bold) },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text(translate("driver_name", viewModel)) },
                            isError = isNameError,
                            supportingText = { if (isNameError) Text("Name cannot be empty", color = MaterialTheme.colorScheme.error) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text(translate("driver_phone", viewModel)) },
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone),
                            isError = isPhoneError,
                            supportingText = { if (isPhoneError) Text("Enter valid 10+ digit phone", color = MaterialTheme.colorScheme.error) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = vehicleNum,
                            onValueChange = { vehicleNum = it },
                            label = { Text(translate("vehicle_num", viewModel)) },
                            isError = isVehicleNumError,
                            supportingText = { if (isVehicleNumError) Text("Vehicle number cannot be empty", color = MaterialTheme.colorScheme.error) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(translate("vehicle_type", viewModel), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            types.forEach { type ->
                                FilterChip(
                                    selected = vehicleType == type,
                                    onClick = { vehicleType = type },
                                    label = { Text(type) }
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            hasAttemptedSubmit = true
                            val isFormValid = name.isNotBlank() && phone.isNotBlank() && phone.length >= 10 && vehicleNum.isNotBlank()
                            if (isFormValid) {
                                viewModel.addDriver(
                                    Driver(
                                        name = name,
                                        phone = phone,
                                        vehicleNumber = vehicleNum,
                                        vehicleType = vehicleType,
                                        status = DriverStatus.AVAILABLE
                                    )
                                )
                                showAddDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Saffron)
                    ) {
                        Text(translate("save", viewModel))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text(translate("cancel", viewModel))
                    }
                }
            )
        }

        // Actions Dialog for Driver
        selectedDriverForAction?.let { driver ->
            AlertDialog(
                onDismissRequest = { selectedDriverForAction = null },
                title = { Text(driver.name, fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("${driver.vehicleType} • ${driver.vehicleNumber}", style = MaterialTheme.typography.bodyMedium)
                        
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(onClick = { IntentUtils.dialPhoneNumber(context, driver.phone) }) {
                                Icon(Icons.Default.Call, contentDescription = "Call", tint = Saffron)
                            }
                            IconButton(onClick = { IntentUtils.openWhatsAppChat(context, driver.phone, "🙏 Jai Baba Baidyanath!") }) {
                                Icon(Icons.AutoMirrored.Filled.Message, contentDescription = "WhatsApp", tint = SuccessGreen)
                            }
                        }

                        HorizontalDivider()

                        Text(translate("change_status", viewModel), fontWeight = FontWeight.Bold)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listOf(DriverStatus.AVAILABLE, DriverStatus.BUSY, DriverStatus.OFFLINE).forEach { status ->
                                val labelKey = when(status) {
                                    DriverStatus.AVAILABLE -> "status_avail"
                                    DriverStatus.BUSY -> "status_busy"
                                    else -> "status_offline"
                                }
                                FilterChip(
                                    selected = driver.status == status,
                                    onClick = {
                                        viewModel.updateDriver(driver.copy(status = status))
                                        selectedDriverForAction = null
                                    },
                                    label = { Text(translate(labelKey, viewModel)) }
                                )
                            }
                        }

                        if (driver.weeklyCommissionDue > 0.0) {
                            HorizontalDivider()
                            Button(
                                onClick = {
                                    viewModel.updateDriver(driver.copy(
                                        totalCommissionPaid = driver.totalCommissionPaid + driver.weeklyCommissionDue,
                                        weeklyCommissionDue = 0.0
                                    ))
                                    selectedDriverForAction = null
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text(translate("settled", viewModel))
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { selectedDriverForAction = null }) {
                        Text("Close")
                    }
                }
            )
        }
    }

@Composable
fun DriverCard(driver: Driver, onClick: () -> Unit, viewModel: DevaYatraViewModel) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.LightGray.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(44.dp).clip(CircleShape),
                color = Saffron.copy(alpha = 0.15f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = driver.name.take(1),
                        style = MaterialTheme.typography.titleMedium,
                        color = Saffron,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = driver.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                Text(text = "${driver.vehicleType} • ${driver.vehicleNumber}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = WarningYellow, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${driver.averageRating} (${driver.totalRides} ${if (viewModel.currentLanguage.value == "hi") "यात्राएं" else "rides"})", style = MaterialTheme.typography.bodySmall)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                StatusBadge(driver.status, viewModel)
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        shape = androidx.compose.foundation.shape.CircleShape,
                        color = Color(0xFF1976D2), // Vibrant Call Blue
                        contentColor = Color.White,
                        modifier = Modifier
                            .size(48.dp)
                            .clickable { IntentUtils.dialPhoneNumber(context, driver.phone) }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "Call Driver",
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Surface(
                        shape = androidx.compose.foundation.shape.CircleShape,
                        color = Color(0xFF2E7D32), // Vibrant WhatsApp Green
                        contentColor = Color.White,
                        modifier = Modifier
                            .size(48.dp)
                            .clickable { IntentUtils.openWhatsAppChat(context, driver.phone, "🙏 Jai Baba Baidyanath!") }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Message,
                                contentDescription = "WhatsApp Driver",
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: DriverStatus, viewModel: DevaYatraViewModel) {
    val labelKey = when(status) {
        DriverStatus.AVAILABLE -> "status_avail"
        DriverStatus.BUSY -> "status_busy"
        else -> "status_offline"
    }
    Surface(
        shape = CircleShape,
        color = when(status) {
            DriverStatus.AVAILABLE -> SuccessGreen.copy(alpha = 0.2f)
            DriverStatus.BUSY -> Saffron.copy(alpha = 0.2f)
            else -> Color.Gray.copy(alpha = 0.2f)
        }
    ) {
        Text(
            text = translate(labelKey, viewModel),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = when(status) {
                DriverStatus.AVAILABLE -> SuccessGreen
                DriverStatus.BUSY -> Saffron
                else -> Color.Gray
            }
        )
    }
}
