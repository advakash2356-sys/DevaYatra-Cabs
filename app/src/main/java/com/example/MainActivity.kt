package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.data.BookingStatus
import com.example.ui.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val repository = (application as DevaYatraApplication).repository
            val viewModel: DevaYatraViewModel = viewModel(factory = DevaYatraViewModelFactory(repository))
            
            val context = androidx.compose.ui.platform.LocalContext.current
            val sharedPrefs = context.getSharedPreferences("YatraSarthiPrefs", android.content.Context.MODE_PRIVATE)
            val hasConsented = remember { 
                mutableStateOf(sharedPrefs.getBoolean("dpdp_consent", false)) 
            }

            LaunchedEffect(Unit) {
                viewModel.seedData()
            }

            DevaYatraTheme {
                if (!hasConsented.value) {
                    AlertDialog(
                        onDismissRequest = { /* Force consent */ },
                        title = { Text(stringResource(R.string.dpdp_compliance_title)) },
                        text = { Text(stringResource(R.string.dpdp_compliance_message)) },
                        confirmButton = {
                            Button(onClick = {
                                sharedPrefs.edit().putBoolean("dpdp_consent", true).apply()
                                hasConsented.value = true
                            }) {
                                Text("Agree & Continue")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { 
                                // View Privacy Policy
                            }) {
                                Text("View Privacy Policy")
                            }
                        }
                    )
                }
                MainContainer(viewModel, hasConsented)
            }
        }
    }
}

@Composable
fun MainContainer(
    viewModel: DevaYatraViewModel,
    hasConsented: androidx.compose.runtime.MutableState<Boolean>
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val sharedPrefs = context.getSharedPreferences("YatraSarthiPrefs", android.content.Context.MODE_PRIVATE)
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Real-time states for active bookings and pending driver escalations
    val bookings by viewModel.allBookings.collectAsState()
    val activeEscalations by viewModel.activeEscalations.collectAsState()
    val availableDrivers by viewModel.availableDrivers.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()

    val activeBookingsCount = bookings.count { it.status != BookingStatus.DONE && it.status != BookingStatus.CANCELLED }
    val pendingEscalationsCount = activeEscalations.size
    val availableDriversCount = availableDrivers.size

    val items = listOf(
        NavigationItem("Home", Screen.Dashboard, Icons.Default.Dashboard, badgeCount = null),
        NavigationItem("Bookings", Screen.Bookings, Icons.AutoMirrored.Filled.FormatListBulleted, badgeCount = if (activeBookingsCount > 0) activeBookingsCount else null),
        NavigationItem("Drivers", Screen.Drivers, Icons.Default.DirectionsCar, badgeCount = if (availableDriversCount > 0) availableDriversCount else null),
        NavigationItem("Alerts", Screen.Escalations, Icons.Default.Warning, badgeCount = if (pendingEscalationsCount > 0) pendingEscalationsCount else null, isAlert = true)
    )

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useNavRail = maxWidth >= 600.dp

        Row(modifier = Modifier.fillMaxSize()) {
            if (useNavRail) {
                // Material 3 Navigation Rail for Adaptive / Wide / Tablet views
                NavigationRail(
                    modifier = Modifier.fillMaxHeight(),
                    containerColor = MaterialTheme.colorScheme.surface,
                    header = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 12.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Saffron,
                                contentColor = Color.White,
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("🔱", fontSize = 22.sp)
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = translate("nav_rail_title", viewModel),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Black,
                                color = Saffron
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            // Real-time Pulse Indicator
                            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                            val pulseAlpha by infiniteTransition.animateFloat(
                                initialValue = 0.4f,
                                targetValue = 1f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(800, easing = LinearEasing),
                                    repeatMode = RepeatMode.Reverse
                                ),
                                label = "pulseAlpha"
                            )
                            
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SuccessGreen.copy(alpha = 0.15f),
                                modifier = Modifier.padding(horizontal = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(SuccessGreen.copy(alpha = pulseAlpha))
                                    )
                                    Text(
                                        text = translate("live_pulse", viewModel),
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SuccessGreen
                                    )
                                }
                            }
                        }
                    }
                ) {
                    items.forEach { item ->
                        val translatedLabel = when (item.label) {
                            "Home" -> translate("nav_home", viewModel)
                            "Bookings" -> translate("nav_bookings", viewModel)
                            "Drivers" -> translate("nav_drivers", viewModel)
                            "Alerts" -> translate("nav_alerts", viewModel)
                            else -> item.label
                        }
                        val isSelected = currentDestination?.hierarchy?.any { 
                            it.route?.contains(item.screen::class.simpleName ?: "") == true 
                        } == true

                        NavigationRailItem(
                            icon = {
                                BadgedBox(
                                    badge = {
                                        if (item.badgeCount != null && item.badgeCount > 0) {
                                            Badge(
                                                containerColor = if (item.isAlert) AlertRed else if (item.label == "Drivers") SuccessGreen else Saffron,
                                                contentColor = Color.White
                                            ) {
                                                Text(
                                                    text = item.badgeCount.toString(),
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                ) {
                                    Icon(item.icon, contentDescription = translatedLabel)
                                }
                            },
                            label = { Text(translatedLabel, fontSize = 11.sp) },
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.screen) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Language Toggle in Navigation Rail
                    IconButton(
                        onClick = { viewModel.toggleLanguage() },
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Text(
                            text = if (currentLang == "en") "हिन्दी" else "ENG",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Saffron
                        )
                    }

                    // Emergency SOS Shortcut in Navigation Rail
                    IconButton(
                        onClick = { IntentUtils.dialPhoneNumber(context, "112") },
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Emergency SOS",
                            tint = AlertRed
                        )
                    }
                }
            }

            Scaffold(
                modifier = Modifier.weight(1f),
                bottomBar = {
                    if (!useNavRail) {
                        NavigationBar {
                            items.forEach { item ->
                                val translatedLabel = when (item.label) {
                                    "Home" -> translate("nav_home", viewModel)
                                    "Bookings" -> translate("nav_bookings", viewModel)
                                    "Drivers" -> translate("nav_drivers", viewModel)
                                    "Alerts" -> translate("nav_alerts", viewModel)
                                    else -> item.label
                                }
                                val isSelected = currentDestination?.hierarchy?.any { 
                                    it.route?.contains(item.screen::class.simpleName ?: "") == true 
                                } == true

                                NavigationBarItem(
                                    icon = {
                                        BadgedBox(
                                            badge = {
                                                if (item.badgeCount != null && item.badgeCount > 0) {
                                                    Badge(
                                                        containerColor = if (item.isAlert) AlertRed else if (item.label == "Drivers") SuccessGreen else Saffron,
                                                        contentColor = Color.White
                                                    ) {
                                                        Text(
                                                            text = item.badgeCount.toString(),
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }
                                                }
                                            }
                                        ) {
                                            Icon(item.icon, contentDescription = translatedLabel)
                                        }
                                    },
                                    label = { Text(translatedLabel) },
                                    selected = isSelected,
                                    onClick = {
                                        navController.navigate(item.screen) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.Dashboard,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable<Screen.Dashboard> {
                        DashboardScreen(
                            viewModel = viewModel,
                            onNavigateToBookings = { navController.navigate(Screen.Bookings) },
                            onNavigateToDrivers = { navController.navigate(Screen.Drivers) },
                            onNavigateToEscalations = { navController.navigate(Screen.Escalations) },
                            onBookingClick = { bookingId ->
                                navController.navigate(Screen.BookingDetail(bookingId))
                            },
                            onInfoClick = {
                                navController.navigate(Screen.PrivacyPolicy)
                            }
                        )
                    }
                    composable<Screen.Bookings> {
                        BookingsScreen(viewModel) { bookingId ->
                            navController.navigate(Screen.BookingDetail(bookingId))
                        }
                    }
                    composable<Screen.Drivers> {
                        DriversScreen(viewModel)
                    }
                    composable<Screen.Escalations> {
                        EscalationsScreen(viewModel) { bookingId ->
                            navController.navigate(Screen.BookingDetail(bookingId))
                        }
                    }
                    composable<Screen.PrivacyPolicy> {
                        PrivacyPolicyScreen(
                            onBack = { navController.navigateUp() },
                            onWithdrawConsent = {
                                sharedPrefs.edit().remove("dpdp_consent").apply()
                                hasConsented.value = false
                                navController.navigate(Screen.Dashboard) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        )
                    }
                    composable<Screen.BookingDetail> { backStackEntry ->
                        val detail = backStackEntry.toRoute<Screen.BookingDetail>()
                        BookingDetailScreen(
                            bookingId = detail.bookingId,
                            viewModel = viewModel,
                            onBack = { navController.navigateUp() }
                        )
                    }
                }
            }
        }
    }
}

data class NavigationItem(
    val label: String,
    val screen: Any,
    val icon: ImageVector,
    val badgeCount: Int? = null,
    val isAlert: Boolean = false
)
