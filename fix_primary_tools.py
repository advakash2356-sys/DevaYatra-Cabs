import re

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'r') as f:
    content = f.read()

# Update DashboardScreen signature
sig_pattern = r"fun DashboardScreen\(\n    viewModel: DevaYatraViewModel,\n    onBookingClick: \(Long\) -> Unit,\n    onInfoClick: \(\) -> Unit\n\) \{"
new_sig = """fun DashboardScreen(
    viewModel: DevaYatraViewModel,
    onBookingClick: (Long) -> Unit,
    onInfoClick: () -> Unit,
    onNavigateToBookings: () -> Unit,
    onNavigateToDrivers: () -> Unit,
    onNavigateToEscalations: () -> Unit
) {"""
content = content.replace("fun DashboardScreen(\n    viewModel: DevaYatraViewModel,\n    onBookingClick: (Long) -> Unit,\n    onInfoClick: () -> Unit\n) {", new_sig)

# Add PrimaryToolsSection composable
primary_tools_composable = """
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
            Text("🛠️", fontSize = 16.sp)
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
                colors = CardDefaults.cardColors(containerColor = Saffron),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.AddCircle, contentDescription = "New Booking", tint = Color.White, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (currentLang == "hi") "नया बुकिंग" else "New Booking",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                }
            }
            
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavigateToDrivers() },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.Person, contentDescription = "Drivers", tint = Saffron, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (currentLang == "hi") "ड्राइवर" else "Drivers",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavigateToEscalations() },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.Warning, contentDescription = "Escalations", tint = AlertRed, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (currentLang == "hi") "शिकायत" else "Alerts",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
"""
content = content + primary_tools_composable

# Update MainActivity.kt
with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    main_activity = f.read()

main_activity = main_activity.replace(
    "DashboardScreen(viewModel, onBookingClick = { bookingId ->",
    "DashboardScreen(viewModel, onNavigateToBookings = { navController.navigate(Screen.Bookings) }, onNavigateToDrivers = { navController.navigate(Screen.Drivers) }, onNavigateToEscalations = { navController.navigate(Screen.Escalations) }, onBookingClick = { bookingId ->"
)
with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(main_activity)

# Reorder LazyColumn in DashboardScreen.kt again
# I want:
# 1. HeaderSection
# 2. PrimaryToolsSection
# 3. YatraQuickBookingSection
# 4. KpiSection
# 5. Emergency Row
# 6. HeritageSectionLocalized

pattern_lazy_column = r"LazyColumn\([\s\S]*?verticalArrangement = Arrangement\.spacedBy\(20\.dp\)\n        \) \{([\s\S]*?)\n        \}\n    \}"
match = re.search(pattern_lazy_column, content)
if match:
    items_content = match.group(1)
    items = items_content.split("            item {")
    items = [i for i in items if i.strip()]
    
    # Existing order in current content:
    # 0: Header
    # 1: YatraQuickBookingSection
    # 2: KpiSection
    # 3: Emergency Row
    # 4: HeritageSectionLocalized
    
    header = "            item {" + items[0]
    primary_tools = "\n            item {\n                PrimaryToolsSection(viewModel, onNavigateToBookings, onNavigateToDrivers, onNavigateToEscalations)\n            }\n"
    quick_booking = "            item {" + items[1]
    kpi = "            item {" + items[2]
    emergency = "            item {" + items[3]
    heritage = "            item {" + items[4]
    
    new_items_content = "\n" + header + primary_tools + quick_booking + kpi + emergency + heritage
    new_lazy_column = "LazyColumn(\n            modifier = Modifier\n                .fillMaxSize()\n                .padding(innerPadding)\n                .padding(horizontal = 16.dp),\n            verticalArrangement = Arrangement.spacedBy(20.dp)\n        ) {" + new_items_content + "\n        }\n    }"
    
    content = content.replace(match.group(0), new_lazy_column)

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'w') as f:
    f.write(content)
