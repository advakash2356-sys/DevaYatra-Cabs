package com.example.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Booking
import com.example.data.BookingStatus
import com.example.ui.theme.Saffron
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingsScreen(
    viewModel: DevaYatraViewModel,
    onBookingClick: (Long) -> Unit
) {
    val bookings by viewModel.allBookings.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val tabs = listOf(
        translate("tab_all", viewModel),
        translate("tab_pending", viewModel),
        translate("tab_active", viewModel),
        translate("tab_done", viewModel)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(translate("manage_bookings", viewModel), fontWeight = FontWeight.Bold) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Saffron,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = translate("new_booking", viewModel))
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            // Search Bar with Voice affordance (Low literacy friendly)
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text(if (viewModel.currentLanguage.value == "hi") "खोजने के लिए टाइप करें..." else "Search by ID, name or location...") },
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

            // Date Quick Filters (Low literacy / High speed)
            var selectedDateFilter by remember { mutableStateOf("All") }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "Today", "Tomorrow").forEach { label ->
                    val isSelected = selectedDateFilter == label
                    val displayLabel = if (viewModel.currentLanguage.value == "hi") {
                        when(label) { "All" -> "सभी"; "Today" -> "आज"; else -> "कल" }
                    } else label
                    
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedDateFilter = label },
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                    ) {
                        Text(
                            text = displayLabel,
                            modifier = Modifier.padding(vertical = 8.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // High-contrast, beautifully accessible pill chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    Surface(
                        modifier = Modifier.clickable { selectedTab = index },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) Saffron else Saffron.copy(alpha = 0.08f),
                        contentColor = if (isSelected) Color.White else Saffron
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            val icon = when(index) {
                                1 -> Icons.Default.Timer
                                2 -> Icons.Default.DirectionsCar
                                3 -> Icons.Default.CheckCircle
                                else -> Icons.AutoMirrored.Filled.List
                            }
                            Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = title, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            val filteredByTab = when(selectedTab) {
                1 -> bookings.filter { it.status == BookingStatus.PENDING }
                2 -> bookings.filter { it.status == BookingStatus.ASSIGNED || it.status == BookingStatus.ACTIVE }
                3 -> bookings.filter { it.status == BookingStatus.DONE }
                else -> bookings
            }
            
            // Apply Date Filter Logic
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val tomorrow = Date(System.currentTimeMillis() + 86400000)
            val tomorrowDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(tomorrow)

            val filteredByDate = when(selectedDateFilter) {
                "Today" -> filteredByTab.filter { it.travelDate == currentDate }
                "Tomorrow" -> filteredByTab.filter { it.travelDate == tomorrowDate }
                else -> filteredByTab
            }

            val filteredBookings = if (searchQuery.isBlank()) {
                filteredByDate
            } else {
                val query = searchQuery.trim().lowercase(Locale.getDefault())
                filteredByDate.filter {
                    it.bookingId.lowercase(Locale.getDefault()).contains(query) ||
                    it.customerName.lowercase(Locale.getDefault()).contains(query) ||
                    it.customerPhone.lowercase(Locale.getDefault()).contains(query) ||
                    it.pickupLocation.lowercase(Locale.getDefault()).contains(query) ||
                    it.destination.lowercase(Locale.getDefault()).contains(query) ||
                    it.travelDate.lowercase(Locale.getDefault()).contains(query)
                }
            }

            if (filteredBookings.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(translate("no_bookings", viewModel), style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (filteredBookings.isEmpty()) { item { EmptyStateView(title = if (viewModel.currentLanguage.value == "hi") "कोई बुकिंग नहीं" else "No bookings found", message = if (viewModel.currentLanguage.value == "hi") "इस फ़िल्टर के लिए कोई बुकिंग नहीं है" else "No bookings match your current filters") } } else { items(filteredBookings) { booking ->
                        SummaryBookingCard(booking, onClick = { onBookingClick(booking.id) }, viewModel = viewModel)
                    }
                    }
                    }
                }
            }
        }

        if (showAddDialog) {
            var name by remember { mutableStateOf("") }
            var phone by remember { mutableStateOf("") }
            var pickup by remember { mutableStateOf("") }
            var destination by remember { mutableStateOf("") }
            var date by remember { mutableStateOf("") }
            var time by remember { mutableStateOf("") }
            var passengers by remember { mutableStateOf("4") }
            var fare by remember { mutableStateOf("") }
            var commissionPercent by remember { mutableStateOf("15") }
            var notes by remember { mutableStateOf("") }
            var vehicleType by remember { mutableStateOf("Sedan") }
            val vehicleTypes = listOf("Sedan", "SUV", "Hatchback", "Innova", "Auto")

            // Validation states
            var hasAttemptedSubmit by remember { mutableStateOf(false) }

            val isNameError = hasAttemptedSubmit && name.isBlank()
            val isPhoneError = hasAttemptedSubmit && (phone.isBlank() || phone.length < 10)
            val isPickupError = hasAttemptedSubmit && pickup.isBlank()
            val isDestinationError = hasAttemptedSubmit && destination.isBlank()
            val isFareError = hasAttemptedSubmit && (fare.toDoubleOrNull() ?: 0.0) <= 0.0

            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text(translate("new_booking", viewModel), fontWeight = FontWeight.Bold) },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text(translate("cust_name", viewModel)) },
                            isError = isNameError,
                            supportingText = { if (isNameError) Text("Name cannot be empty", color = MaterialTheme.colorScheme.error) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text(translate("cust_phone", viewModel)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            isError = isPhoneError,
                            supportingText = { if (isPhoneError) Text("Enter valid 10+ digit phone", color = MaterialTheme.colorScheme.error) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = pickup,
                            onValueChange = { pickup = it },
                            label = { Text(translate("pickup_loc", viewModel)) },
                            isError = isPickupError,
                            supportingText = { if (isPickupError) Text("Pickup location cannot be empty", color = MaterialTheme.colorScheme.error) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        // Quick pickup presets
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                        ) {
                            val pickups = listOf("Jasidih Junction", "Baba Mandir", "Deoghar Station")
                            pickups.forEach { p ->
                                SuggestionChip(
                                    onClick = { pickup = p },
                                    label = { Text(p, fontSize = 10.sp) }
                                )
                            }
                        }
                        
                        OutlinedTextField(
                            value = destination,
                            onValueChange = { destination = it },
                            label = { Text(translate("drop_loc", viewModel)) },
                            isError = isDestinationError,
                            supportingText = { if (isDestinationError) Text("Destination cannot be empty", color = MaterialTheme.colorScheme.error) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        // Quick destination presets
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                        ) {
                            val destinations = listOf("Baba Mandir", "Basukinath", "Trikut Pahar")
                            destinations.forEach { d ->
                                SuggestionChip(
                                    onClick = { destination = d },
                                    label = { Text(d, fontSize = 10.sp) }
                                )
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = date,
                                onValueChange = { date = it },
                                label = { Text(translate("travel_date", viewModel)) },
                                modifier = Modifier.weight(1f),
                                placeholder = { Text("YYYY-MM-DD") }
                            )
                            OutlinedTextField(
                                value = time,
                                onValueChange = { time = it },
                                label = { Text(translate("travel_time", viewModel)) },
                                modifier = Modifier.weight(1f),
                                placeholder = { Text("10:00 AM") }
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = passengers,
                                onValueChange = { passengers = it },
                                label = { Text(translate("num_passengers", viewModel)) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = fare,
                                onValueChange = { fare = it },
                                label = { Text(translate("quoted_fare", viewModel)) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                isError = isFareError,
                                supportingText = { if (isFareError) Text("Enter valid fare", color = MaterialTheme.colorScheme.error) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = commissionPercent,
                                onValueChange = { commissionPercent = it },
                                label = { Text("Commission %") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(Modifier.weight(1f))
                        }
                        OutlinedTextField(
                            value = notes,
                            onValueChange = { notes = it },
                            label = { Text("Special Notes / Instructions") },
                            placeholder = { Text("e.g., Carrying heavy luggage, elder passengers") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3
                        )
                        Text(translate("vehicle_type", viewModel), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            vehicleTypes.forEach { type ->
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
                            val parsedFare = fare.toDoubleOrNull() ?: 0.0
                            val parsedPassengers = passengers.toIntOrNull() ?: 4
                            val commissionRateValue = (commissionPercent.toDoubleOrNull() ?: 15.0) / 100.0
                            
                            val isFormValid = name.isNotBlank() && 
                                              phone.isNotBlank() && phone.length >= 10 &&
                                              pickup.isNotBlank() && 
                                              destination.isNotBlank() && 
                                              parsedFare > 0.0

                            if (isFormValid) {
                                val generatedId = "DYC-${Random.nextInt(100, 999)}"
                                val bookingDate = if (date.isBlank()) {
                                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                                } else date
                                val bookingTime = if (time.isBlank()) "12:00 PM" else time

                                viewModel.addBooking(
                                    Booking(
                                        bookingId = generatedId,
                                        customerName = name,
                                        customerPhone = phone,
                                        pickupLocation = pickup,
                                        destination = destination,
                                        travelDate = bookingDate,
                                        travelTime = bookingTime,
                                        passengers = parsedPassengers,
                                        quotedFare = parsedFare,
                                        commission = parsedFare * commissionRateValue,
                                        status = BookingStatus.PENDING,
                                        notes = if (notes.isBlank()) null else notes
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
    }
