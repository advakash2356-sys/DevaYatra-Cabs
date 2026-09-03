import re

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'r') as f:
    content = f.read()

# Make the header background BloodRed and the border Saffron
content = content.replace('colors = CardDefaults.cardColors(containerColor = Saffron.copy(alpha = 0.05f)),', 'colors = CardDefaults.cardColors(containerColor = BloodRed),')
content = content.replace('border = androidx.compose.foundation.BorderStroke(1.dp, Saffron.copy(alpha = 0.2f))', 'border = androidx.compose.foundation.BorderStroke(2.dp, Marigold)')

# Change text colors in the header
content = content.replace('color = Saffron,', 'color = Marigold,')

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'w') as f:
    f.write(content)

with open('app/src/main/java/com/example/ui/Localization.kt', 'r') as f:
    content = f.read()

content = content.replace('"welcome" to "WELCOME BACK,",', '"welcome" to "ॐ नमः शिवाय",')
content = content.replace('"welcome" to "स्वागत है,",', '"welcome" to "ॐ नमः शिवाय",')

content = content.replace('"dashboard_title" to "Manage Yatra\\nBookings",', '"dashboard_title" to "Baba Yatra\\nBookings",')
content = content.replace('"dashboard_title" to "यात्रा बुकिंग\\nमैनेज करें",', '"dashboard_title" to "बाबा यात्रा\\nबुकिंग",')

with open('app/src/main/java/com/example/ui/Localization.kt', 'w') as f:
    f.write(content)
