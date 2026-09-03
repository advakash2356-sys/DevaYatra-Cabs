import re

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'r') as f:
    content = f.read()

# Change emergency banner colors to ShivBlue and BloodRed
content = content.replace('Color(0xFFD32F2F)', 'BloodRed')
content = content.replace('Color(0xFF2E7D32)', 'ShivBlue')

# Adjust KpiCard border colors to be more prominent
content = content.replace('Color.LightGray.copy(alpha = 0.4f)', 'BhasmGray')
content = content.replace('SuccessGreen.copy(alpha = 0.3f)', 'SuccessGreen')
content = content.replace('Saffron.copy(alpha = 0.3f)', 'Saffron')

# Make the QuickBooking Card border darker
content = content.replace('Saffron.copy(alpha = 0.15f)', 'Saffron.copy(alpha = 0.5f)')
content = content.replace('Saffron.copy(alpha = 0.02f)', 'Color.Transparent')

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'w') as f:
    f.write(content)
