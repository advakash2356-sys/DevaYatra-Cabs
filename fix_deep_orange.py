import re

with open('app/src/main/java/com/example/ui/BookingDetailScreen.kt', 'r') as f:
    content = f.read()

content = content.replace('DeepOrange', 'BloodRed')

with open('app/src/main/java/com/example/ui/BookingDetailScreen.kt', 'w') as f:
    f.write(content)

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'r') as f:
    content = f.read()

content = content.replace('DeepOrange', 'BloodRed')

with open('app/src/main/java/com/example/ui/DashboardScreen.kt', 'w') as f:
    f.write(content)
