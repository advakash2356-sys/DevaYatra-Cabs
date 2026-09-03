package com.example.ui

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Booking
import com.example.data.BookingStatus
import com.example.data.EscalationLevel
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

data class QuickBookingTemplate(
    val name: String,
    val nameHi: String,
    val pickup: String,
    val drop: String,
    val fare: Double,
    val passengers: Int,
    val vehicleType: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DevaYatraViewModel,
    onBookingClick: (Long) -> Unit,
    onInfoClick: () -> Unit,
    onNavigateToBookings: () -> Unit,
    onNavigateToDrivers: () -> Unit,
    onNavigateToEscalations: () -> Unit
) {
    val bookings by viewModel.allBookings.collectAsState()
    val activeEscalations by viewModel.activeEscalations.collectAsState()
    val availableDrivers by viewModel.availableDrivers.collectAsState()
    val allDrivers by viewModel.allDrivers.collectAsState()
    val completedCount by viewModel.completedCount.collectAsState()
    val totalCommission by viewModel.totalCommission.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()
    val context = LocalContext.current

    // Calculate Real-Time Active Bookings Statistics
    val activeBookings = bookings.filter { it.status != BookingStatus.DONE && it.status != BookingStatus.CANCELLED }
    val pendingAssignmentCount = activeBookings.count { it.status == BookingStatus.PENDING }
    val assignedCount = activeBookings.count { it.status == BookingStatus.ASSIGNED }
    val inProgressCount = activeBookings.count { it.status == BookingStatus.ACTIVE }
    val totalActivePassengers = activeBookings.sumOf { it.passengers }
    val totalActiveFare = activeBookings.sumOf { it.quotedFare }

    // Calculate Real-Time Pending Driver Escalations Statistics
    val pendingEscalations = activeEscalations
    val urgentEscalationsCount = pendingEscalations.count { it.escalationLevel == EscalationLevel.URGENT }

    var activeQuickBookingConfirm by remember { mutableStateOf<QuickBookingTemplate?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = translate("app_name", viewModel),
                            fontWeight = FontWeight.Black,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                },
                actions = {
                    // Language Switcher Chip
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable { viewModel.toggleLanguage() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Translate,
                                contentDescription = "Language",
                                modifier = Modifier.size(16.dp),
                                tint = Saffron
                            )
                            Text(
                                text = if (currentLang == "en") "हिन्दी" else "English",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelMedium,
                                color = Saffron
                            )
                        }
                    }

                    IconButton(onClick = onInfoClick) {
                        Icon(Icons.Default.Info, contentDescription = "Privacy Info")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. Heritage Header Banner
            item {
                HeaderSection(viewModel)
            }

            // 2. Real-Time Telemetry Command Bar with Live Beacon
            item {
                LiveTelemetryBar(
                    activeCount = activeBookings.size,
                    escalationCount = pendingEscalations.size,
                    availableFleetCount = availableDrivers.size,
                    viewModel = viewModel
                )
            }

            // 3. High-Priority Pending Driver Escalations Control Hub
            item {
                PendingDriverEscalationsHub(
                    pendingEscalations = pendingEscalations,
                    urgentCount = urgentEscalationsCount,
                    viewModel = viewModel,
                    onNavigateToEscalations = onNavigateToEscalations,
                    onBookingClick = onBookingClick
                )
            }

            // 4. Real-Time Active Bookings Telemetry & Radar
            item {
                ActiveBookingsTelemetrySection(
                    activeBookings = activeBookings,
                    pendingAssignmentCount = pendingAssignmentCount,
                    assignedCount = assignedCount,
                    inProgressCount = inProgressCount,
                    totalPassengers = totalActivePassengers,
                    totalActiveFare = totalActiveFare,
                    viewModel = viewModel,
                    onNavigateToBookings = onNavigateToBookings,
                    onBookingClick = onBookingClick
                )
            }

            // 5. Fleet Operational Distribution Gauge
            item {
                FleetCapacityBar(
                    totalDrivers = allDrivers.size,
                    availableDrivers = availableDrivers.size,
                    activeBookingsCount = activeBookings.size,
                    viewModel = viewModel,
                    onNavigateToDrivers = onNavigateToDrivers
                )
            }

            // 6. Overall Performance KPI Section
            item {
                KpiSection(
                    totalBookings = bookings.size,
                    completedRides = completedCount,
                    totalEarnings = totalCommission ?: 0.0,
                    viewModel = viewModel
                )
            }

            // 7. Primary Action Tools
            item {
                PrimaryToolsSection(viewModel, onNavigateToBookings, onNavigateToDrivers, onNavigateToEscalations)
            }

            // 8. Quick Yatra Pilgrimage Route Shortcuts
            item {
                YatraQuickBookingSection(viewModel) { template ->
                    activeQuickBookingConfirm = template
                }
            }

            // 9. Emergency SOS & 24x7 Helpdesk
            item {
                EmergencySupportSection(currentLang, context)
            }

            // 10. Heritage Trust Section
            item {
                HeritageSectionLocalized(viewModel)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Quick Booking Confirmation Dialog
    activeQuickBookingConfirm?.let { template ->
        val isHi = currentLang == "hi"
        val dialogTitle = if (isHi) "त्वरित यात्रा बुकिंग पुष्टि" else "Confirm Quick Yatra Booking"
        val dialogMsg = if (isHi) {
            "क्या आप ${template.nameHi} के लिए ₹${template.fare.toInt()} का नया बुकिंग स्लॉट बनाना चाहते हैं?\n\nपैसेंजर: ${template.passengers}\nवाहन प्रकार: ${template.vehicleType}"
        } else {
            "Do you want to instantly create a new booking for ${template.name} at ₹${template.fare.toInt()}?\n\nPassengers: ${template.passengers}\nVehicle Type: ${template.vehicleType}"
        }

        AlertDialog(
            onDismissRequest = { activeQuickBookingConfirm = null },
            title = { Text(dialogTitle, fontWeight = FontWeight.Bold) },
            text = { Text(dialogMsg) },
            confirmButton = {
                Button(
                    onClick = {
                        val generatedId = "DYC-${Random.nextInt(100, 999)}"
                        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                        val currentTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
                        
                        viewModel.addBooking(
                            Booking(
                                bookingId = generatedId,
                                customerName = if (isHi) "तीर्थयात्री (Walk-in)" else "Walk-in Pilgrim",
                                customerPhone = "+91 94711 55667",
                                pickupLocation = template.pickup,
                                destination = template.drop,
                                travelDate = currentDate,
                                travelTime = currentTime,
                                passengers = template.passengers,
                                quotedFare = template.fare,
                                commission = template.fare * 0.15,
                                status = BookingStatus.PENDING,
                                notes = "YatraSarthi Fast Dispatch"
                            )
                        )
                        activeQuickBookingConfirm = null
                        Toast.makeText(context, if (isHi) "बुकिंग सफलतापूर्वक बनाई गई!" else "Booking created successfully!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Saffron)
                ) {
                    Text(if (isHi) "हाँ, बुक करें" else "Yes, Book Now")
                }
            },
            dismissButton = {
                TextButton(onClick = { activeQuickBookingConfirm = null }) {
                    Text(if (isHi) "रद्द करें" else "Cancel")
                }
            }
        )
    }
}

/**
 * 🟢 Live Telemetry Bar with animated pulsing indicator
 */
@Composable
fun LiveTelemetryBar(
    activeCount: Int,
    escalationCount: Int,
    availableFleetCount: Int,
    viewModel: DevaYatraViewModel
) {
    val infiniteTransition = rememberInfiniteTransition(label = "beacon")
    val beaconAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "beaconAlpha"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DeepShivBlue),
        border = androidx.compose.foundation.BorderStroke(1.dp, Saffron.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(SuccessGreen.copy(alpha = beaconAlpha))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = translate("live_monitor", viewModel),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (escalationCount > 0) AlertRed.copy(alpha = 0.2f) else SuccessGreen.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (escalationCount > 0) AlertRed else SuccessGreen
                    )
                ) {
                    Text(
                        text = if (escalationCount > 0) "$escalationCount ${translate("nav_alerts", viewModel)}" else translate("all_clear", viewModel),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (escalationCount > 0) AlertRed else SuccessGreen
                    )
                }
            }

            // Stat Badges Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TelemetryMetricPill(
                    label = translate("live_status_active", viewModel),
                    count = activeCount.toString(),
                    color = Saffron,
                    icon = Icons.Default.DirectionsCar,
                    modifier = Modifier.weight(1f)
                )
                TelemetryMetricPill(
                    label = translate("pending_escalations_title", viewModel),
                    count = escalationCount.toString(),
                    color = if (escalationCount > 0) AlertRed else BhasmDark,
                    icon = Icons.Default.Warning,
                    modifier = Modifier.weight(1f)
                )
                TelemetryMetricPill(
                    label = translate("fleet_ready_capacity", viewModel),
                    count = availableFleetCount.toString(),
                    color = SuccessGreen,
                    icon = Icons.Default.CheckCircle,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun TelemetryMetricPill(
    label: String,
    count: String,
    color: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.08f),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, color.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(16.dp)
            )
            Column {
                Text(
                    text = count,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = color
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.7f),
                    maxLines = 1,
                    fontSize = 9.sp
                )
            }
        }
    }
}

/**
 * 🚨 Real-Time Pending Driver Escalations Control Hub
 */
@Composable
fun PendingDriverEscalationsHub(
    pendingEscalations: List<Booking>,
    urgentCount: Int,
    viewModel: DevaYatraViewModel,
    onNavigateToEscalations: () -> Unit,
    onBookingClick: (Long) -> Unit
) {
    val context = LocalContext.current
    val currentLang = viewModel.currentLanguage.collectAsState().value
    val resolvedToastText = translate("alert_resolved_toast", viewModel)

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = if (pendingEscalations.isNotEmpty()) AlertRed else SuccessGreen,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                SectionTitle(
                    title = translate("pending_escalations_title", viewModel),
                    color = if (pendingEscalations.isNotEmpty()) AlertRed else MaterialTheme.colorScheme.onBackground
                )
            }

            if (pendingEscalations.isNotEmpty()) {
                TextButton(onClick = onNavigateToEscalations) {
                    Text(
                        text = translate("view_all_alerts", viewModel),
                        color = AlertRed,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

        if (pendingEscalations.isEmpty()) {
            // Calm, operational green card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SuccessGreen.copy(alpha = 0.08f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = SuccessGreen,
                        contentColor = Color.White,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(20.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (currentLang == "hi") "🔱 सब कुशल मंगल" else "All Clear & Operating Smoothly",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = translate("no_pending_escalations", viewModel),
                            style = MaterialTheme.typography.bodySmall,
                            color = BhasmDark
                        )
                    }
                }
            }
        } else {
            // Urgent Escalation Cards
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                pendingEscalations.take(3).forEach { booking ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onBookingClick(booking.id) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (booking.escalationLevel == EscalationLevel.URGENT) AlertRed.copy(alpha = 0.08f) else WarningYellow.copy(alpha = 0.08f)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.5.dp,
                            if (booking.escalationLevel == EscalationLevel.URGENT) AlertRed else WarningYellow
                        )
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (booking.escalationLevel == EscalationLevel.URGENT) AlertRed else WarningYellow,
                                        contentColor = Color.White
                                    ) {
                                        Text(
                                            text = if (booking.escalationLevel == EscalationLevel.URGENT) "URGENT SOS" else "ALERT",
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Black
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = booking.bookingId,
                                        fontWeight = FontWeight.Black,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Text(
                                    text = "₹${booking.quotedFare.toInt()}",
                                    fontWeight = FontWeight.ExtraBold,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Saffron
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "${booking.pickupLocation} ➔ ${booking.destination}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${booking.customerName} (${booking.customerPhone})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = BhasmDark
                                )
                                if (booking.driverName != null) {
                                    Text(
                                        text = "Driver: ${booking.driverName}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = BhasmDark,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Action buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        viewModel.resolveEscalation(booking.id)
                                        Toast.makeText(context, resolvedToastText, Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1.2f),
                                    contentPadding = PaddingValues(vertical = 8.dp)
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = translate("resolve_alert_action", viewModel),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }

                                OutlinedButton(
                                    onClick = { IntentUtils.dialPhoneNumber(context, booking.customerPhone) },
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f),
                                    contentPadding = PaddingValues(vertical = 8.dp)
                                ) {
                                    Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Call", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }

                                OutlinedButton(
                                    onClick = {
                                        IntentUtils.openWhatsAppChat(
                                            context,
                                            booking.customerPhone,
                                            "Jai Baba Baidyanath! YatraSarthi regarding booking ${booking.bookingId}"
                                        )
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f),
                                    contentPadding = PaddingValues(vertical = 8.dp)
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.Message, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Chat", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EscalationCard(booking: Booking, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = AlertRed.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AlertRed.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = AlertRed,
                contentColor = Color.White,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(18.dp))
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${booking.bookingId}: Pending Attention",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = AlertRed
                )
                Text(
                    text = "${booking.pickupLocation} → ${booking.destination}",
                    style = MaterialTheme.typography.bodySmall,
                    color = BhasmDark
                )
            }
        }
    }
}

/**
 * 🚗 Real-Time Active Bookings Telemetry & Live Radar
 */
@Composable
fun ActiveBookingsTelemetrySection(
    activeBookings: List<Booking>,
    pendingAssignmentCount: Int,
    assignedCount: Int,
    inProgressCount: Int,
    totalPassengers: Int,
    totalActiveFare: Double,
    viewModel: DevaYatraViewModel,
    onNavigateToBookings: () -> Unit,
    onBookingClick: (Long) -> Unit
) {
    val currentLang = viewModel.currentLanguage.collectAsState().value

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.FormatListBulleted,
                    contentDescription = null,
                    tint = Saffron,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                SectionTitle(
                    title = "${translate("active_bookings_title", viewModel)} (${activeBookings.size})"
                )
            }

            TextButton(onClick = onNavigateToBookings) {
                Text(
                    text = translate("view_all_active", viewModel),
                    color = Saffron,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        // Breakdown Cards Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatusTelemetryCard(
                count = pendingAssignmentCount,
                label = translate("live_status_pending_assign", viewModel),
                color = AlertRed,
                icon = Icons.Default.AccessTime,
                modifier = Modifier.weight(1f)
            )
            StatusTelemetryCard(
                count = assignedCount,
                label = translate("live_status_assigned", viewModel),
                color = ShivBlue,
                icon = Icons.Default.Person,
                modifier = Modifier.weight(1f)
            )
            StatusTelemetryCard(
                count = inProgressCount,
                label = translate("live_status_in_progress", viewModel),
                color = SuccessGreen,
                icon = Icons.Default.DirectionsCar,
                modifier = Modifier.weight(1f)
            )
        }

        // Live Volume Summary Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            border = androidx.compose.foundation.BorderStroke(1.dp, Saffron.copy(alpha = 0.3f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Saffron.copy(alpha = 0.15f),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Groups, contentDescription = null, tint = Saffron, modifier = Modifier.size(22.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "$totalPassengers ${translate("pilgrims_en_route", viewModel)}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = translate("active_fare_volume", viewModel),
                            style = MaterialTheme.typography.bodySmall,
                            color = BhasmDark
                        )
                    }
                }

                Text(
                    text = "₹${totalActiveFare.toInt()}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = Saffron
                )
            }
        }

        // Live Active Booking Cards List
        if (activeBookings.isEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, BhasmGray.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = BhasmDark,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (currentLang == "hi") "वर्तमान में कोई सक्रिय बुकिंग नहीं है" else "No active trips right now",
                        style = MaterialTheme.typography.bodyMedium,
                        color = BhasmDark,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                activeBookings.take(4).forEach { booking ->
                    SummaryBookingCard(
                        booking = booking,
                        onClick = { onBookingClick(booking.id) },
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun StatusTelemetryCard(
    count: Int,
    label: String,
    color: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = color.copy(alpha = 0.08f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = color
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                fontSize = 11.sp
            )
        }
    }
}

/**
 * ⚡ Fleet Capacity & Distribution Live Meter
 */
@Composable
fun FleetCapacityBar(
    totalDrivers: Int,
    availableDrivers: Int,
    activeBookingsCount: Int,
    viewModel: DevaYatraViewModel,
    onNavigateToDrivers: () -> Unit
) {
    val currentLang = viewModel.currentLanguage.collectAsState().value
    val busyDrivers = (totalDrivers - availableDrivers).coerceAtLeast(0)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigateToDrivers() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = Marigold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (currentLang == "hi") "फ्लीट क्षमता और उपलब्धता" else "Fleet Capacity & Real-Time Availability",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = "$availableDrivers / $totalDrivers Ready",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = SuccessGreen
                )
            }

            // Visual Progress / Allocation Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color.LightGray.copy(alpha = 0.3f))
            ) {
                val availableWeight = if (totalDrivers > 0) availableDrivers.toFloat() / totalDrivers else 1f
                val busyWeight = if (totalDrivers > 0) busyDrivers.toFloat() / totalDrivers else 0f

                if (availableWeight > 0f) {
                    Box(
                        modifier = Modifier
                            .weight(availableWeight.coerceAtLeast(0.01f))
                            .fillMaxHeight()
                            .background(SuccessGreen)
                    )
                }
                if (busyWeight > 0f) {
                    Box(
                        modifier = Modifier
                            .weight(busyWeight.coerceAtLeast(0.01f))
                            .fillMaxHeight()
                            .background(Saffron)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(SuccessGreen)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$availableDrivers ${if (currentLang == "hi") "उपलब्ध (Available)" else "Available"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = BhasmDark
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Saffron)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$busyDrivers ${if (currentLang == "hi") "व्यस्त/सफ़र में" else "Busy / On Trip"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = BhasmDark
                    )
                }
            }
        }
    }
}

/**
 * 🕉️ Quick Yatra Booking Pre-set Shortcuts
 */
@Composable
fun YatraQuickBookingSection(
    viewModel: DevaYatraViewModel,
    onTemplateClick: (QuickBookingTemplate) -> Unit
) {
    val currentLang = viewModel.currentLanguage.collectAsState().value
    val templates = listOf(
        QuickBookingTemplate(
            name = "Jasidih ⇄ Baba Mandir",
            nameHi = "जसीडीह ⇄ बाबा मंदिर",
            pickup = "Jasidih Junction",
            drop = "Baba Mandir, Deoghar",
            fare = 500.0,
            passengers = 4,
            vehicleType = "Sedan"
        ),
        QuickBookingTemplate(
            name = "Deoghar ⇄ Basukinath",
            nameHi = "देवघर ⇄ बासुकीनाथ धाम",
            pickup = "Baba Mandir, Deoghar",
            drop = "Basukinath Temple, Dumka",
            fare = 1500.0,
            passengers = 6,
            vehicleType = "SUV"
        ),
        QuickBookingTemplate(
            name = "Local Darshan Tour",
            nameHi = "लोकल देवघर दर्शन",
            pickup = "Deoghar Hotel",
            drop = "Trikut Pahar & Naulakha",
            fare = 1200.0,
            passengers = 4,
            vehicleType = "Sedan"
        )
    )

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("🕉️", fontSize = 20.sp)
            Spacer(modifier = Modifier.width(6.dp))
            SectionTitle(translate("yatra_quick_booking", viewModel))
        }
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(templates) { template ->
                Card(
                    modifier = Modifier
                        .width(230.dp)
                        .clickable { onTemplateClick(template) },
                    colors = CardDefaults.cardColors(containerColor = DeepShivBlue),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = Saffron.copy(alpha = 0.5f)
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = if (currentLang == "hi") template.nameHi else template.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Saffron
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${template.vehicleType} • ${template.passengers} ${if (currentLang == "hi") "सीटें" else "Seats"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = BhasmDark
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "₹${template.fare.toInt()}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Marigold,
                                contentColor = Color.White
                            ) {
                                Text(
                                    text = if (currentLang == "hi") "बुक करें" else "Book",
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 🔱 Heritage Header Section
 */
@Composable
fun HeaderSection(viewModel: DevaYatraViewModel) {
    val isHi = viewModel.currentLanguage.collectAsState().value == "hi"
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = BloodRed),
        border = androidx.compose.foundation.BorderStroke(2.dp, Marigold)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = translate("welcome", viewModel),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Marigold,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = translate("dashboard_title", viewModel),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    lineHeight = 26.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isHi) "🔱 बाबा धाम की सेवा में • राजा भैया" else "🔱 In the Service of Baba Dham • Raja Bhaiya",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = "🔱",
                fontSize = 32.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

/**
 * 📊 Performance KPI Cards
 */
@Composable
fun KpiSection(
    totalBookings: Int,
    completedRides: Int,
    totalEarnings: Double,
    viewModel: DevaYatraViewModel
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        KpiCard(
            label = translate("tab_all", viewModel),
            value = totalBookings.toString(),
            icon = Icons.Default.Book,
            borderColor = BhasmGray,
            modifier = Modifier.weight(1f)
        )
        KpiCard(
            label = translate("tab_done", viewModel),
            value = completedRides.toString(),
            icon = Icons.Default.CheckCircle,
            borderColor = SuccessGreen,
            modifier = Modifier.weight(1f)
        )
        KpiCard(
            label = translate("kpi_earned", viewModel),
            value = "₹${totalEarnings.toInt()}",
            icon = Icons.Default.Payments,
            borderColor = Marigold,
            modifier = Modifier.weight(1.1f)
        )
    }
}

@Composable
fun KpiCard(
    label: String,
    value: String,
    icon: ImageVector,
    borderColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = if (borderColor == BhasmGray) BhasmGray else if (borderColor == SuccessGreen) SuccessGreen else Marigold
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = BhasmDark,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun SectionTitle(title: String, color: Color = MaterialTheme.colorScheme.onBackground) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Black,
        color = color,
        letterSpacing = 0.2.sp
    )
}

/**
 * 📋 Booking Summary Card for Live Radar
 */
@Composable
fun SummaryBookingCard(booking: Booking, onClick: () -> Unit, viewModel: DevaYatraViewModel) {
    val context = LocalContext.current
    val currentLang = viewModel.currentLanguage.collectAsState().value
    
    val statusColor = when(booking.status) {
        BookingStatus.DONE -> SuccessGreen
        BookingStatus.PENDING -> AlertRed
        BookingStatus.ACTIVE -> Saffron
        else -> ShivBlue
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.LightGray.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = statusColor.copy(alpha = 0.12f),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = when(booking.status) {
                                BookingStatus.DONE -> Icons.Default.CheckCircle
                                BookingStatus.PENDING -> Icons.Default.AccessTime
                                BookingStatus.ACTIVE -> Icons.Default.DirectionsCar
                                else -> Icons.Default.Person
                            },
                            contentDescription = null,
                            tint = statusColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = booking.bookingId,
                            fontWeight = FontWeight.Black,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (booking.escalationLevel != EscalationLevel.NORMAL) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = AlertRed
                            ) {
                                Text(
                                    text = "!",
                                    color = Color.White,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = booking.customerName,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = BhasmDark
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${booking.pickupLocation} → ${booking.destination}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹${booking.quotedFare.toInt()}",
                        fontWeight = FontWeight.Black,
                        style = MaterialTheme.typography.titleMedium,
                        color = Saffron
                    )
                    Text(
                        text = when(booking.status) {
                            BookingStatus.PENDING -> if (currentLang == "hi") "लंबित" else "Pending"
                            BookingStatus.ASSIGNED -> if (currentLang == "hi") "नियुक्त" else "Assigned"
                            BookingStatus.ACTIVE -> if (currentLang == "hi") "सक्रिय" else "Active"
                            BookingStatus.DONE -> if (currentLang == "hi") "पूर्ण" else "Done"
                            BookingStatus.CANCELLED -> if (currentLang == "hi") "रद्द" else "Cancelled"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = statusColor,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF1976D2),
                        contentColor = Color.White,
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { IntentUtils.dialPhoneNumber(context, booking.customerPhone) }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "Call Passenger",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    
                    Surface(
                        shape = CircleShape,
                        color = ShivBlue,
                        contentColor = Color.White,
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { IntentUtils.openWhatsAppChat(context, booking.customerPhone, "Jai Baba Baidyanath!") }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Message,
                                contentDescription = "WhatsApp Passenger",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 🛠️ Primary Tools Section
 */
@Composable
fun PrimaryToolsSection(
    viewModel: DevaYatraViewModel,
    onNavigateToBookings: () -> Unit,
    onNavigateToDrivers: () -> Unit,
    onNavigateToEscalations: () -> Unit
) {
    val currentLang = viewModel.currentLanguage.collectAsState().value
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("🔱", fontSize = 20.sp, color = Saffron)
            Spacer(modifier = Modifier.width(6.dp))
            SectionTitle(if (currentLang == "hi") "मुख्य उपकरण (Primary Tools)" else "Primary Tools")
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavigateToBookings() },
                colors = CardDefaults.cardColors(containerColor = BloodRed),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.AddCircle, contentDescription = "New Booking", tint = Marigold, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (currentLang == "hi") "नया बुकिंग" else "New Booking",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp
                    )
                }
            }
            
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavigateToDrivers() },
                colors = CardDefaults.cardColors(containerColor = DeepShivBlue),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.Person, contentDescription = "Drivers", tint = Color.White, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (currentLang == "hi") "ड्राइवर" else "Drivers",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavigateToEscalations() },
                colors = CardDefaults.cardColors(containerColor = DeepShivBlue),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.Warning, contentDescription = "Escalations", tint = AlertRed, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (currentLang == "hi") "शिकायत" else "Alerts",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

/**
 * 🆘 Emergency Support Section
 */
@Composable
fun EmergencySupportSection(currentLang: String, context: android.content.Context) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Card(
            modifier = Modifier
                .weight(1f)
                .clickable { IntentUtils.dialPhoneNumber(context, "112") },
            colors = CardDefaults.cardColors(containerColor = BloodRed),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.PhoneInTalk, contentDescription = "Emergency Support", tint = Color.White, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (currentLang == "hi") "आपातकालीन SOS (112)" else "Emergency SOS (112)",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp
                )
            }
        }

        Card(
            modifier = Modifier
                .weight(1f)
                .clickable { IntentUtils.openWhatsAppChat(context, "919876543210", "Need help with Baba Dham Yatra Booking") },
            colors = CardDefaults.cardColors(containerColor = DeepShivBlue),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.AutoMirrored.Filled.Message, contentDescription = "WhatsApp Support", tint = Color.White, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (currentLang == "hi") "बाबा धाम WhatsApp" else "Baba Dham Chat",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp
                )
            }
        }
    }
}
