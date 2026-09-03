import re

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'r') as f:
    content = f.read()

# Update HeaderSection
content = content.replace('Text("जय शिव,")', 'Text("ॐ नमः शिवाय",')
content = content.replace('Text("Welcome Back,")', 'Text("Har Har Mahadev,")')

# Change Primary Tools section emoji
content = content.replace('Text("🛠️", fontSize = 16.sp)', 'Text("🔱", fontSize = 20.sp, color = Saffron)')

# In PrimaryToolsSection change icon colors to BloodRed and Marigold
content = content.replace('Icon(Icons.Default.AddCircle, contentDescription = "New Booking", tint = Color.White', 'Icon(Icons.Default.AddCircle, contentDescription = "New Booking", tint = Marigold')
content = content.replace('colors = CardDefaults.cardColors(containerColor = Saffron),', 'colors = CardDefaults.cardColors(containerColor = BloodRed),')

content = content.replace('colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),', 'colors = CardDefaults.cardColors(containerColor = ShivBlue),')
content = content.replace('Icon(Icons.Default.Person, contentDescription = "Drivers", tint = Saffron, modifier = Modifier.size(32.dp))', 'Icon(Icons.Default.Person, contentDescription = "Drivers", tint = Color.White, modifier = Modifier.size(32.dp))')
content = content.replace('color = MaterialTheme.colorScheme.onSurface,', 'color = Color.White,')

# YatraQuickBookingSection emoji
content = content.replace('Text("⚡", fontSize = 16.sp)', 'Text("🕉️", fontSize = 20.sp)')

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'w') as f:
    f.write(content)

