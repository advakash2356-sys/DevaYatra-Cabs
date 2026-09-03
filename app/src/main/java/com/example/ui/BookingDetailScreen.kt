package com.example.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.NotificationImportant
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BookingStatus
import com.example.data.DriverStatus
import com.example.data.EscalationLevel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDetailScreen(
    bookingId: Long,
    viewModel: DevaYatraViewModel,
    onBack: () -> Unit
) {
    val bookings by viewModel.allBookings.collectAsState()
    val drivers by viewModel.allDrivers.collectAsState()
    val booking = bookings.find { it.id == bookingId }
    val context = LocalContext.current
    val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current
    
    var showAssignDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(booking?.bookingId ?: translate("booking_details", viewModel)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (booking == null) {
            Box(Modifier.padding(innerPadding).fillMaxSize()) {
                Text(translate("not_found", viewModel))
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Urgent Escalation Banner
                if (booking.escalationLevel == EscalationLevel.URGENT) {
                    Surface(
                        color = AlertRed,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = androidx.compose.ui.graphics.Color.White)
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = if (viewModel.currentLanguage.value == "hi") "ध्यान दें: यह बुकिंग तुरंत कार्रवाई के लिए है!" else "URGENT ATTENTION REQUIRED: This booking is escalated!",
                                color = androidx.compose.ui.graphics.Color.White,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                StatusHeader(booking.status, viewModel)
                
                DetailSection(title = translate("cust_details", viewModel), viewModel = viewModel) {
                    DetailRow(
                        label = translate("cust_name", viewModel),
                        value = booking.customerName
                    )
                    DetailRow(
                        label = translate("cust_phone", viewModel),
                        value = booking.customerPhone,
                        isPhone = true,
                        onPhoneClick = { IntentUtils.dialPhoneNumber(context, booking.customerPhone) }
                    )
                }

                DetailSection(title = translate("trip_details", viewModel), viewModel = viewModel) {
                    DetailRow(
                        label = translate("pickup_loc", viewModel),
                        value = booking.pickupLocation,
                        isLocation = true,
                        onLocationClick = { IntentUtils.openInGoogleMaps(context, booking.pickupLocation) }
                    )
                    DetailRow(
                        label = translate("drop_loc", viewModel),
                        value = booking.destination,
                        isLocation = true,
                        onLocationClick = { IntentUtils.openInGoogleMaps(context, booking.destination) }
                    )
                    DetailRow(translate("travel_date", viewModel), booking.travelDate)
                    DetailRow(translate("travel_time", viewModel), booking.travelTime)
                    DetailRow(translate("num_passengers", viewModel), booking.passengers.toString())
                }

                if (booking.notes != null) {
                    DetailSection(title = "Special Notes / Instructions", viewModel = viewModel) {
                        Text(
                            text = booking.notes,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }

                DetailSection(title = translate("fare_commission", viewModel), viewModel = viewModel) {
                    DetailRow(translate("quoted_fare", viewModel), "₹${booking.quotedFare}")
                    DetailRow(translate("comm_rate", viewModel), "₹${booking.commission}")
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            IntentUtils.openUPIPayment(
                                context,
                                upiId = "merchant@upi", // Replace with real UPI ID
                                name = "YatraSarthi",
                                amount = booking.quotedFare.toString(),
                                note = "Booking ID ${booking.bookingId}"
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00838F)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (viewModel.currentLanguage.value == "hi") "UPI से भुगतान लें" else "Request UPI Payment",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // If a driver is assigned, display details
                if (booking.driverName != null) {
                    val linkedDriver = drivers.find { it.name == booking.driverName }
                    val isCapacityIssue = booking.passengers > 4 && 
                        (linkedDriver?.vehicleType == "Sedan" || linkedDriver?.vehicleType == "Hatchback" || linkedDriver?.vehicleType == "Auto")

                    DetailSection(title = translate("driver_details", viewModel), viewModel = viewModel) {
                        DetailRow(translate("driver_name", viewModel), booking.driverName)
                        DetailRow(translate("vehicle_num", viewModel), booking.vehicleNumber ?: "N/A")
                        if (linkedDriver != null) {
                            DetailRow("Vehicle Type", linkedDriver.vehicleType)
                            DetailRow(
                                label = translate("driver_phone", viewModel),
                                value = linkedDriver.phone,
                                isPhone = true,
                                onPhoneClick = { IntentUtils.dialPhoneNumber(context, linkedDriver.phone) }
                            )
                        }
                        
                        if (isCapacityIssue) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                color = AlertRed.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, AlertRed),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Warning, contentDescription = null, tint = AlertRed, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "WARNING: Assigned vehicle capacity may be insufficient for ${booking.passengers} passengers!",
                                        color = AlertRed,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                // WhatsApp Automation Cards
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = translate("whatsapp_actions", viewModel),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Saffron
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Notify Customer on WhatsApp
                            Button(
                                onClick = {
                                    val currentLang = viewModel.currentLanguage.value
                                    val message = if (currentLang == "hi") {
                                        "यात्रासार्थी बुकिंग पुष्टि:\nबुकिंग आईडी: ${booking.bookingId}\nपिकअप: ${booking.pickupLocation} से ${booking.destination}\nड्राइवर का नाम: ${booking.driverName ?: "लंबित"}\nगाड़ी का नंबर: ${booking.vehicleNumber ?: "लंबित"}\nयात्रा सुखद और मंगलमय हो! जय बाबा बैद्यनाथ!"
                                    } else {
                                        "YatraSarthi Booking Confirmed:\nBooking ID: ${booking.bookingId}\nTrip: ${booking.pickupLocation} to ${booking.destination}\nDriver: ${booking.driverName ?: "Pending"}\nVehicle: ${booking.vehicleNumber ?: "Pending"}\nHave a blessed journey! Jai Baba Baidyanath!"
                                    }
                                    IntentUtils.openWhatsAppChat(context, booking.customerPhone, message)
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Saffron)
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Message, contentDescription = null)
                                Spacer(Modifier.width(4.dp))
                                Text(translate("notify_customer_wa", viewModel), style = MaterialTheme.typography.labelSmall)
                            }

                            // Notify Driver on WhatsApp (if assigned)
                            if (booking.driverName != null) {
                                val linkedDriver = drivers.find { it.name == booking.driverName }
                                Button(
                                    onClick = {
                                        if (linkedDriver != null) {
                                            val currentLang = viewModel.currentLanguage.value
                                            val message = if (currentLang == "hi") {
                                                "यात्रासार्थी नई यात्रा:\nयात्रा आईडी: ${booking.bookingId}\nपिकअप: ${booking.pickupLocation} → ${booking.destination}\nसमय: ${booking.travelDate} ${booking.travelTime}\nग्राहक का नाम: ${booking.customerName} (${booking.customerPhone})\nसमय पर पहुँचें। जय बाबा बैद्यनाथ!"
                                            } else {
                                                "YatraSarthi Assigned Trip:\nTrip ID: ${booking.bookingId}\nRoute: ${booking.pickupLocation} → ${booking.destination}\nTime: ${booking.travelDate} ${booking.travelTime}\nCustomer: ${booking.customerName} (${booking.customerPhone})\nPlease arrive on time. Jai Baba Baidyanath!"
                                            }
                                            IntentUtils.openWhatsAppChat(context, linkedDriver.phone, message)
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.Message, contentDescription = null)
                                    Spacer(Modifier.width(4.dp))
                                    Text(translate("notify_driver_wa", viewModel), style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }

                // Digital Yatra Slip Receipt Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Marigold.copy(alpha = 0.05f)),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = Marigold.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "🎫 " + translate("copy_slip", viewModel),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = BloodRed
                            )
                            Icon(
                                Icons.Default.Share,
                                contentDescription = null,
                                tint = Saffron,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        
                        val assignedVehicleType = drivers.find { it.id == booking.driverId }?.vehicleType ?: "Sedan/SUV Required"
                        val slipCopiedText = translate("slip_copied", viewModel)

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            val slipText = buildString {
                                appendLine("✨ DEVA YATRA CABS ✨")
                                appendLine("🙏 JAI BABA BAIDYANATH 🙏")
                                appendLine("--------------------------------")
                                appendLine("🎫 ID: ${booking.bookingId}")
                                appendLine("👤 Passenger: ${booking.customerName}")
                                appendLine("📞 Phone: ${booking.customerPhone}")
                                appendLine("📍 Pickup: ${booking.pickupLocation}")
                                appendLine("🏁 Drop: ${booking.destination}")
                                appendLine("📅 Date/Time: ${booking.travelDate} | ${booking.travelTime}")
                                appendLine("🚗 Car Type: $assignedVehicleType")
                                if (booking.driverName != null) {
                                    appendLine("👤 Driver: ${booking.driverName}")
                                    appendLine("🚙 Number: ${booking.vehicleNumber}")
                                } else {
                                    appendLine("👤 Driver: Pending Assignment")
                                }
                                appendLine("💵 Fare: ₹${booking.quotedFare.toInt()}")
                                appendLine("--------------------------------")
                                appendLine("Have a safe and divine journey!")
                            }
                            
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = slipText,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                    lineHeight = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                // One-Tap Direct WhatsApp Share (Zero copy-paste required!)
                                Button(
                                    onClick = {
                                        IntentUtils.openWhatsAppChat(context, booking.customerPhone, slipText)
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (viewModel.currentLanguage.value == "hi") "व्हाट्सएप पर रसीद भेजें" else "Send via WhatsApp",
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Android Native Share
                                    Button(
                                        onClick = {
                                            IntentUtils.shareText(context, slipText, "Yatra Slip")
                                        },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Icon(Icons.Default.Share, contentDescription = null, tint = Color.White)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (viewModel.currentLanguage.value == "hi") "शेयर करें" else "Share",
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                    
                                    // Clipboard Copy
                                    OutlinedButton(
                                        onClick = {
                                            clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(slipText))
                                            android.widget.Toast.makeText(context, slipCopiedText, android.widget.Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Saffron)
                                    ) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Saffron)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (viewModel.currentLanguage.value == "hi") "कॉपी करें" else "Copy",
                                            fontWeight = FontWeight.Bold,
                                            color = Saffron
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Escalation Toggle Button
                if (booking.escalationLevel == EscalationLevel.URGENT) {
                    Button(
                        onClick = {
                            viewModel.resolveEscalation(booking.id)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                    ) {
                        Icon(Icons.Default.NotificationImportant, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(translate("de_escalate", viewModel))
                    }
                } else {
                    OutlinedButton(
                        onClick = {
                            viewModel.updateBooking(booking.copy(escalationLevel = EscalationLevel.URGENT))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AlertRed)
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(translate("escalate_alert", viewModel))
                    }
                }

                HorizontalDivider()

                // Driver Assign or Mark Completed Buttons
                if (booking.status == BookingStatus.DONE) {
                    DetailSection(title = translate("cust_rating", viewModel), viewModel = viewModel) {
                        DetailRow(translate("cust_rating", viewModel), "${booking.rating ?: "5"} / 5")
                    }
                } else if (booking.status == BookingStatus.PENDING) {
                    Button(
                        onClick = { showAssignDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Saffron)
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(translate("assign_driver", viewModel))
                    }
                }

                // Reassign/Unassign Driver Button
                if (booking.driverName != null && booking.status != BookingStatus.DONE && booking.status != BookingStatus.CANCELLED) {
                    OutlinedButton(
                        onClick = {
                            val linkedDriver = drivers.find { it.name == booking.driverName }
                            if (linkedDriver != null) {
                                viewModel.updateDriver(linkedDriver.copy(status = DriverStatus.AVAILABLE))
                            }
                            val updatedBooking = booking.copy(
                                status = BookingStatus.PENDING,
                                driverId = null,
                                driverName = null,
                                vehicleNumber = null
                            )
                            viewModel.updateBooking(updatedBooking)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Saffron)
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Unassign / Reassign Driver")
                    }
                }

                // Mark Completed
                if (booking.status == BookingStatus.ACTIVE || booking.status == BookingStatus.ASSIGNED) {
                    Button(
                        onClick = { 
                            val updatedBooking = booking.copy(status = BookingStatus.DONE, rating = 5)
                            viewModel.updateBooking(updatedBooking)
                            
                            // Also update linked driver to increment rides, add commission, and free status
                            if (booking.driverName != null) {
                                val driver = drivers.find { it.name == booking.driverName }
                                if (driver != null) {
                                    viewModel.updateDriver(driver.copy(
                                        status = DriverStatus.AVAILABLE,
                                        totalRides = driver.totalRides + 1,
                                        weeklyCommissionDue = driver.weeklyCommissionDue + booking.commission
                                    ))
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                    ) {
                        Icon(Icons.Default.Done, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(translate("mark_completed", viewModel))
                    }
                }

                // Cancel Booking Option
                if (booking.status != BookingStatus.DONE && booking.status != BookingStatus.CANCELLED) {
                    Button(
                        onClick = {
                            val linkedDriver = if (booking.driverName != null) drivers.find { it.name == booking.driverName } else null
                            if (linkedDriver != null) {
                                viewModel.updateDriver(linkedDriver.copy(status = DriverStatus.AVAILABLE))
                            }
                            val updatedBooking = booking.copy(
                                status = BookingStatus.CANCELLED,
                                driverId = null,
                                driverName = null,
                                vehicleNumber = null
                            )
                            viewModel.updateBooking(updatedBooking)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = AlertRed)
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = androidx.compose.ui.graphics.Color.White)
                        Spacer(Modifier.width(8.dp))
                        Text("Cancel Booking", color = androidx.compose.ui.graphics.Color.White)
                    }
                }
            }
        }

        // Available Drivers Dialogue Selection list
        if (showAssignDialog) {
            val availableDrivers = drivers.filter { it.status == DriverStatus.AVAILABLE }

            AlertDialog(
                onDismissRequest = { showAssignDialog = false },
                title = { Text(translate("select_driver", viewModel), fontWeight = FontWeight.Bold) },
                text = {
                    if (availableDrivers.isEmpty()) {
                        Text(translate("no_drivers_avail", viewModel))
                    } else {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 300.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            availableDrivers.forEach { driver ->
                                val isTooSmall = booking?.let { b ->
                                    b.passengers > 4 && (driver.vehicleType == "Sedan" || driver.vehicleType == "Hatchback" || driver.vehicleType == "Auto")
                                } ?: false

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            booking?.let { b ->
                                                val updatedBooking = b.copy(
                                                    status = BookingStatus.ASSIGNED,
                                                    driverId = driver.id,
                                                    driverName = driver.name,
                                                    vehicleNumber = driver.vehicleNumber
                                                )
                                                viewModel.updateBooking(updatedBooking)
                                                viewModel.updateDriver(driver.copy(status = DriverStatus.BUSY))
                                            }
                                            showAssignDialog = false
                                        },
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isTooSmall) AlertRed.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                    ),
                                    border = if (isTooSmall) androidx.compose.foundation.BorderStroke(1.dp, AlertRed.copy(alpha = 0.5f)) else null
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(12.dp)
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(driver.name, fontWeight = FontWeight.Bold)
                                            Text("${driver.vehicleType} • ${driver.vehicleNumber}", style = MaterialTheme.typography.bodySmall)
                                            if (isTooSmall) {
                                                Text(
                                                    text = "⚠️ Small for ${booking?.passengers ?: 4} passengers",
                                                    color = AlertRed,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Saffron)
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showAssignDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun StatusHeader(status: BookingStatus, viewModel: DevaYatraViewModel) {
    val statusKey = when(status) {
        BookingStatus.PENDING -> "tab_pending"
        BookingStatus.DONE -> "tab_done"
        else -> "tab_active"
    }

    Surface(
        color = Saffron.copy(alpha = 0.1f),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Current Status", fontWeight = FontWeight.Bold)
            Text(translate(statusKey, viewModel), color = Saffron, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun DetailSection(title: String, viewModel: DevaYatraViewModel, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Saffron)
        Spacer(Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
        ) {
            Column(Modifier.padding(12.dp)) {
                content()
            }
        }
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String,
    isPhone: Boolean = false,
    onPhoneClick: (() -> Unit)? = null,
    isLocation: Boolean = false,
    onLocationClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                if (isPhone && onPhoneClick != null) {
                    Surface(
                        shape = androidx.compose.foundation.shape.CircleShape,
                        color = Color(0xFF1976D2), // Highly recognizable solid phone blue
                        contentColor = Color.White,
                        modifier = Modifier
                            .size(48.dp)
                            .clickable { onPhoneClick() }
                    ) {
                        Box(contentAlignment = androidx.compose.ui.Alignment.Center) {
                            Icon(
                                Icons.Default.Call,
                                contentDescription = "Call",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
                if (isLocation && onLocationClick != null) {
                    Surface(
                        shape = androidx.compose.foundation.shape.CircleShape,
                        color = Color(0xFFE65100), // High-contrast orange for Map/Directions
                        contentColor = Color.White,
                        modifier = Modifier
                            .size(48.dp)
                            .clickable { onLocationClick() }
                    ) {
                        Box(contentAlignment = androidx.compose.ui.Alignment.Center) {
                            Icon(
                                Icons.Default.Map,
                                contentDescription = "Open Map",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
