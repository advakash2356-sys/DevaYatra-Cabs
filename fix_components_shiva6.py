import re

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'r') as f:
    content = f.read()

# Make the QuickBooking Card background ShivBlue for better contrast with Saffron
content = content.replace('containerColor = ShivBlue', 'containerColor = DeepShivBlue')
content = content.replace('borderColor = Saffron.copy(alpha = 0.5f)', 'borderColor = Marigold.copy(alpha = 0.5f)')
content = content.replace('if (borderColor == Saffron.copy(alpha = 0.5f)) Saffron', 'if (borderColor == Marigold.copy(alpha = 0.5f)) Marigold')

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'w') as f:
    f.write(content)

