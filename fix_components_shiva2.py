import re

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'r') as f:
    content = f.read()

# Make the QuickBooking Card background ShivBlue for better contrast with Saffron
content = content.replace('colors = CardDefaults.cardColors(\n                        containerColor = Color.Transparent\n                    )', 'colors = CardDefaults.cardColors(\n                        containerColor = ShivBlue\n                    )')

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'w') as f:
    f.write(content)
